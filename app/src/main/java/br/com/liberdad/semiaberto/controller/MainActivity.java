package br.com.liberdad.semiaberto.controller;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.liberdad.semiaberto.R;
import br.com.liberdad.semiaberto.model.Comparador;
import br.com.liberdad.semiaberto.model.Contrato;
import br.com.liberdad.semiaberto.model.Expediente;

public class MainActivity extends AppCompatActivity {

    private ListView marcacoesListView;
    private ListViewAdapter listViewAdapter;

    private TextView saioAsTextView;
    private TextView atrasoTextView;
    private TextView debitoTextView;
    private TextView tempoTextView;
    private SeekBar bancoHorasSeekBar;
    //private RadioGroup.OnCheckedChangeListener jornadaOnCheckedChangeListener;
    private CompoundButton.OnCheckedChangeListener contratoOnCheckedChangeListener;
    private AdView mAdView;

    private Expediente expediente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolBar);
        toolBar.setTitle(R.string.app_name);
        toolBar.setTitleTextColor(0xFFFFFFFF);

        marcacoesListView = (ListView) findViewById(R.id.marcacoesListView);
        saioAsTextView = (TextView) findViewById(R.id.saioAsTextView);
        atrasoTextView = (TextView) findViewById(R.id.atrasoTextView);
        debitoTextView = (TextView) findViewById(R.id.debitoTextView);
        tempoTextView = (TextView) findViewById(R.id.tempoTextView);

        bancoHorasSeekBar = (SeekBar) findViewById(R.id.bancoHorasSeekBar);
        bancoHorasSeekBar.setMax(300);
        bancoHorasSeekBar.setProgress(180);
        bancoHorasSeekBar.setOnSeekBarChangeListener(new JornadaOnSeekBarChangeListener(this));

        contratoOnCheckedChangeListener = new ContratoOnCheckedChangeListener(this);
        Switch contratoSwitch = (Switch) findViewById(R.id.contratoSwitch);
        contratoSwitch.setOnCheckedChangeListener(contratoOnCheckedChangeListener);

        listViewAdapter = new ListViewAdapter(this);
        marcacoesListView.setAdapter(listViewAdapter);
        marcacoesListView.setDivider(null);
        marcacoesListView.setDividerHeight(0);

        expediente = new Expediente();
        //expediente.setJornada(8);
        //expediente.setContrato(8);

        mAdView = (AdView) findViewById(R.id.adView);

        //AdRequest adRequest = new AdRequest.Builder().build(); // PROD
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build(); // DEV

        mAdView.loadAd(adRequest);

    }

    @Override
    protected void onPause() {

        if (mAdView != null) {
            mAdView.pause();
        }

        super.onPause();
    }

    @Override
    protected void onResume() {

        if (mAdView != null) {
            mAdView.resume();
        }

        super.onResume();
    }

    @Override
    protected void onDestroy() {

        if (mAdView != null) {
            mAdView.destroy();
        }

        super.onDestroy();
    }


    public void adicionarMarcacao(View view) {
        DialogFragment dialogFragment = new TimePickerFragment();
        dialogFragment.show(getFragmentManager(), "TimePicker");

    }

    public void setMarcacao(int hora, int minuto) {

        Calendar calendario = Calendar.getInstance();
        calendario.set(Calendar.HOUR_OF_DAY, hora);
        calendario.set(Calendar.MINUTE, minuto);
        calendario.set(Calendar.SECOND, 0);
        calendario.set(Calendar.MILLISECOND, 0);

        Date marcacao;
        marcacao = calendario.getTime();

        listViewAdapter.add(marcacao);
        listViewAdapter.sort(new Comparador<Date>());

    }

    public void setMarcacoes(List<Date> marcacoes) {

        expediente.setMarcacoes(marcacoes);
        atualizarSaioAs(expediente.getUltimaSaidaProposta());
        verificarAtrasosEDebitos();

    }


    public void setJornada(int jornada) {

        setTempoTextView(bancoHorasSeekBar.getProgress());

        if (expediente.getContrato() == Contrato.OITO) {

            jornada += (5 * 60);

        } else {

            jornada += (4 * 60);

        }

        expediente.setJornada(jornada * 60 * 1000);
        atualizarSaioAs(expediente.getUltimaSaidaProposta());
        verificarAtrasosEDebitos();
    }

    public void setContrato(int contrato) {

        expediente.setContrato(contrato);

        LinearLayout label6LinearLayout = (LinearLayout) findViewById(R.id.seisLinearLayout);
        LinearLayout label8LinearLayout = (LinearLayout) findViewById(R.id.oitoLinearLayout);

        if (contrato == 8) { // indo do contrato de 6 para 8
            label6LinearLayout.setVisibility(View.GONE);
            label8LinearLayout.setVisibility(View.VISIBLE);

            bancoHorasSeekBar.setMax(300);
            bancoHorasSeekBar.setProgress(bancoHorasSeekBar.getProgress() + 60);

            expediente.setJornada( ( bancoHorasSeekBar.getProgress() + 5 * 60 ) * 60 * 1000 );
        } else {
            label8LinearLayout.setVisibility(View.GONE);
            label6LinearLayout.setVisibility(View.VISIBLE);
            int posicaoAnterior = bancoHorasSeekBar.getProgress();
            bancoHorasSeekBar.setMax(240);

            if (posicaoAnterior < 60) {
                bancoHorasSeekBar.setProgress(0);
            } else {
                bancoHorasSeekBar.setProgress(posicaoAnterior - 60);
            }

            expediente.setJornada( ( bancoHorasSeekBar.getProgress() + 4 * 60 ) * 60 * 1000 );

        }

        setTempoTextView(bancoHorasSeekBar.getProgress());

        atualizarSaioAs(expediente.getUltimaSaidaProposta());
        verificarAtrasosEDebitos();

    }

    private void setTempoTextView(int minutos) {

        int posicaoRelativa = 0;
        String saida = "";

        if (expediente.getContrato() == Contrato.OITO) {

            posicaoRelativa = minutos - 180;

        } else {

            posicaoRelativa = minutos - 120;

        }

        if (posicaoRelativa < 0) {
            saida = "-";
        }

        saida = saida + "0" + Math.abs(posicaoRelativa / 60) + ":";
        if ((Math.abs(posicaoRelativa) % 60) < 10) {
            saida = saida + "0";
        }

        saida = saida + (Math.abs(posicaoRelativa) % 60);

        tempoTextView.setText(saida);

    }

    public void atualizarSaioAs(Date horario) {

        if (null == horario) {
            saioAsTextView.setText(R.string.saioas_text);
        } else {

            SimpleDateFormat formata = new SimpleDateFormat("HH:mm");
            saioAsTextView.setText(formata.format(horario));
        }
    }

    private void verificarAtrasosEDebitos() {

        if (expediente.isAtrasado()) {
            atrasoTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.orange_200));
        } else {
            atrasoTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray_white));
        }
        if (expediente.isDebito()) {
            debitoTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.orange_200));
        } else {
            debitoTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray_white));
        }

    }

    public void decrementarSeekBar(View view) {

        if (bancoHorasSeekBar.getProgress() > 0) {
            bancoHorasSeekBar.setProgress(bancoHorasSeekBar.getProgress() - 1);
            setJornada(bancoHorasSeekBar.getProgress());
        }

    }

    public void incrementarSeekBar(View view) {

        if (bancoHorasSeekBar.getProgress() < bancoHorasSeekBar.getMax()) {
            bancoHorasSeekBar.setProgress(bancoHorasSeekBar.getProgress() + 1);
            setJornada(bancoHorasSeekBar.getProgress());
        }
    }

}
