package br.com.liberdad.semiaberto.controller;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import br.com.liberdad.semiaberto.R;

import static android.content.Context.MODE_PRIVATE;

public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        Intent alarmeIntent = new Intent(context,AlarmeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,1,alarmeIntent,0);

        Uri alarmeUri = RingtoneManager.getDefaultUri(R.raw.lere);

        android.support.v4.app.NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setSmallIcon(R.mipmap.semiaberto)
                        .setContentTitle("Semiaberto")
                        .setContentText("Mete o pé!")
                        .setVibrate(new long[]{1000,1000})
                        .setFullScreenIntent(pendingIntent,true);

        if(intent.getBooleanExtra("sonoro",false)) {
            builder.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/raw/lere"));
        }
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }
}
