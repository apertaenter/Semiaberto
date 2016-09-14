package br.com.liberdad.semiaberto.controller;

import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import br.com.liberdad.semiaberto.R;

public class JornadaOnCheckedChangeListener  implements RadioGroup.OnCheckedChangeListener{

    private AppCompatActivity activity;
    public JornadaOnCheckedChangeListener(AppCompatActivity activity){
        this.activity = activity;
    }
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        int jornada;
        switch(checkedId) {
            case R.id.jornada5h : jornada=5; break;
            case R.id.jornada6h : jornada=6; break;
            default: jornada=8;
        };

        MainActivity mainActivity = (MainActivity) activity;

        mainActivity.setJornada(jornada);

    }
}