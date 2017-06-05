package br.com.liberdad.semiaberto.controller;

import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.Toast;

public class JornadaOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

    private AppCompatActivity activity;
    private int valorAtual = 0;

    public JornadaOnSeekBarChangeListener(AppCompatActivity activity){
        this.activity = activity;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

        valorAtual = i;
        MainActivity mainActivity = (MainActivity) activity;
        mainActivity.setTempoTextView(valorAtual);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        MainActivity mainActivity = (MainActivity) activity;

        mainActivity.setJornada(valorAtual);

    }

}
