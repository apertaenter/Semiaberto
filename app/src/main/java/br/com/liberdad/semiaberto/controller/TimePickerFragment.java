package br.com.liberdad.semiaberto.controller;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

import java.util.Calendar;

import static android.app.TimePickerDialog.*;

public class TimePickerFragment extends DialogFragment implements OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),this,hour,minute,true);
        timePickerDialog.setTitle("Selecione o horário");
        timePickerDialog.setButton(BUTTON_NEGATIVE,"Cancelar",timePickerDialog);
        timePickerDialog.setButton(BUTTON_POSITIVE,"OK",timePickerDialog);

        return timePickerDialog;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        Activity activity = getActivity();
        MainActivity mainActivity = (MainActivity) activity;
        mainActivity.setMarcacao(hourOfDay,minute);

    }
}
