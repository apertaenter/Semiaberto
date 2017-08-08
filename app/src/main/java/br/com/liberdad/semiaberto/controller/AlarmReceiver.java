package br.com.liberdad.semiaberto.controller;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import br.com.liberdad.semiaberto.AlarmeActivity;

public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        Intent alarmeIntent = new Intent(context,AlarmeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,1,alarmeIntent,0);

        android.support.v4.app.NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setFullScreenIntent(pendingIntent,true);

        //Toast.makeText(context, "I'm running", Toast.LENGTH_LONG).show();


    }
}
