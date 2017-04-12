package br.com.liberdad.semiaberto.controller;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import br.com.liberdad.semiaberto.model.Expediente;

public class MainActivity extends AppCompatActivity {

    private ListView marcacoesListView;
    private ListViewAdapter listViewAdapter;

    private TextView saioAsTextView;
    private TextView atrasoTextView;
    private TextView debitoTextView;
    private RadioGroup.OnCheckedChangeListener jornadaOnCheckedChangeListener;
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

        jornadaOnCheckedChangeListener = new JornadaOnCheckedChangeListener(this);
        RadioGroup jornadasRadioGroup = (RadioGroup) findViewById(R.id.jornadas);
        jornadasRadioGroup.setOnCheckedChangeListener(jornadaOnCheckedChangeListener);

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

        AdRequest adRequest = new AdRequest.Builder().build(); // PROD
        //AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build(); // DEV

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

        expediente.setJornada(jornada);
        atualizarSaioAs(expediente.getUltimaSaidaProposta());
        verificarAtrasosEDebitos();
    }

    public void setContrato(int contrato) {

        RadioButton jornada4RadioButton = (RadioButton) findViewById(R.id.jornada4h);
        RadioButton jornada5RadioButton = (RadioButton) findViewById(R.id.jornada5h);
        RadioButton jornada6RadioButton = (RadioButton) findViewById(R.id.jornada6h);
        RadioButton jornada8RadioButton = (RadioButton) findViewById(R.id.jornada8h);

        if (contrato == 8) {
            if (jornada4RadioButton.isChecked())
                jornada5RadioButton.setChecked(true);
            jornada4RadioButton.setEnabled(false);
            jornada8RadioButton.setEnabled(true);
        }else{
            if (jornada8RadioButton.isChecked())
                jornada6RadioButton.setChecked(true);
            jornada4RadioButton.setEnabled(true);
            jornada8RadioButton.setEnabled(false);
        }

        expediente.setContrato(contrato);
        atualizarSaioAs(expediente.getUltimaSaidaProposta());
        verificarAtrasosEDebitos();

    }
    public void atualizarSaioAs(Date horario) {

        if (null == horario) {
            saioAsTextView.setText(R.string.saioas_text);
        } else {

            SimpleDateFormat formata = new SimpleDateFormat("HH:mm");
            saioAsTextView.setText(formata.format(horario));
        }
    }

    private void verificarAtrasosEDebitos(){

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

}
