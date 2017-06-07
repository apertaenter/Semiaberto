package br.com.liberdad.semiaberto.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.liberdad.semiaberto.R;

public class ListViewAdapter extends ArrayAdapter<Date> {

    private Context context;

    public ListViewAdapter(Context context) {
        super(context, 0);

        this.context = context;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_marcacoes, null);
        } else {
            view = convertView;
        }

        ImageView sentidoImageView = (ImageView) view.findViewById(R.id.sentidoImageView);
        ImageView excluirMarcacaoImageView = (ImageView) view.findViewById(R.id.excluirMarcacaoImageView);

        if (position % 2 == 0){
            sentidoImageView.setImageResource(R.mipmap.entrada);
        } else {
            sentidoImageView.setImageResource(R.mipmap.saida);
        }
        TextView marcacaoTextView = (TextView) view.findViewById(R.id.marcacaoTextView);


        SimpleDateFormat formata = new SimpleDateFormat("HH:mm");
        marcacaoTextView.setText(formata.format(getItem(position)));

        excluirMarcacaoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(getItem(position));
                notifyDataSetChanged();
            }
        });

        return view;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

        List<Date> marcacoes = new ArrayList<Date>();

        for(int i=0;i<getCount();i++){
            marcacoes.add(getItem(i));
        }

        MainActivity mainActivity = (MainActivity) context;

        mainActivity.setMarcacoes(marcacoes);

    }

}