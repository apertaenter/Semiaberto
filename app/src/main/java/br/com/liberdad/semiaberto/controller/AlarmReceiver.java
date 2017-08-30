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

    private long[] track = new long[74];

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent alarmeIntent = new Intent(context,AlarmeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,1,alarmeIntent,0);

        Uri alarmeUri = RingtoneManager.getDefaultUri(R.raw.lere);

        track[0] = 85;		// 00000 - 00085
        track[1] = 50;		// 00085 - 00135 v 1
        track[2] = 285;		// 00135 - 00420
        track[3] = 50;		// 00420 - 00470 v 2
        track[4] = 270;		// 00470 - 00740
        track[5] = 50;		// 00740 - 00790 v 1
        track[6] = 110;		// 00790 - 00900
        track[7] = 50;		// 00900 - 00950 v 2
        track[8] = 90;		// 00950 - 01040
        track[9] = 50;		// 01040 - 01090 v 3
        track[10] = 290;	// 01090 - 01380
        track[11] = 50;		// 01380 - 01430 v 1a1
        track[12] = 400;	// 01430 - 01830
        track[13] = 50;		// 01830 - 01880 v 1a2
        track[14] = 420;	// 01880 - 02300
        track[15] = 50;		// 02300 - 02350 v 1a3
        track[16] = 920;	// 02350 - 03270
        track[17] = 50;		// 03270 - 03320 v 1a4
        track[18] = 600;	// 03320 - 03920
        track[19] = 50;		// 03920 - 03970 v 1b1
        track[20] = 400;	// 03970 - 04370
        track[21] = 50;		// 04370 - 04420 v 1b2
        track[22] = 430;	// 04420 - 04850
        track[23] = 50;		// 04850 - 04900 v 1b3
        track[24] = 910;	// 04900 - 05810
        track[25] = 50;		// 05810 - 05860 v 1b4
        track[26] = 600;	// 05860 - 06460
        track[27] = 50;		// 06460 - 06510 v 1c1
        track[28] = 440;	// 06510 - 06950
        track[29] = 50;		// 06950 - 07000 v 1c2
        track[30] = 400;	// 07000 - 07400
        track[31] = 50;		// 07400 - 07450 v 1c3
        track[32] = 910;	// 07450 - 08360
        track[33] = 50;		// 08360 - 08410 v 1c4
        track[34] = 570;	// 08410 - 08980
        track[35] = 50;		// 08980 - 09040 v 1d1
        track[36] = 430;	// 09040 - 09470
        track[37] = 50;		// 09470 - 09520 v 1d2
        track[38] = 410;	// 09520 - 09930
        track[39] = 50;		// 09930 - 09980 v 1d3
        track[40] = 910;	// 09980 - 10890
        track[41] = 50;		// 10890 - 10940 v 1d4
        track[42] = 580;	// 10940 - 11520
        track[43] = 50;		// 11520 - 11570 v 2a1
        track[44] = 410;	// 11570 - 11980
        track[45] = 50;		// 11980 - 12030 v 2a2
        track[46] = 400;	// 12030 - 12430
        track[47] = 50;		// 12430 - 12480 v 2a3
        track[48] = 900;	// 12480 - 13380
        track[49] = 50;		// 13380 - 13430 v 2a4
        track[50] = 590;	// 13430 - 14020
        track[51] = 50;		// 14020 - 14070 v 2b1
        track[52] = 360;	// 14070 - 14430
        track[53] = 50;		// 14430 - 14480 v 2b2
        track[54] = 460;	// 14480 - 14940
        track[55] = 50;		// 14940 - 14990 v 2b3
        track[56] = 900;	// 14990 - 15890
        track[57] = 50;		// 15890 - 15940 v 2b4
        track[58] = 590;	// 15940 - 16530
        track[59] = 50;		// 16530 - 16580 v 2c1
        track[60] = 390;	// 16580 - 16970
        track[61] = 50;		// 16970 - 17020 v 2c2
        track[62] = 390;	// 17020 - 17430
        track[63] = 50;		// 17430 - 17480 v 2c3
        track[64] = 910;	// 17480 - 18390
        track[65] = 50;		// 18390 - 18440 v 2c4
        track[66] = 560;	// 18440 - 19000
        track[67] = 50;		// 19000 - 19050 v 2d1
        track[68] = 410;	// 19050 - 19460
        track[69] = 50;		// 19460 - 19510 v 2d2
        track[70] = 400;	// 19510 - 19910
        track[71] = 50;		// 19910 - 19960 v 2d3
        track[72] = 930;	// 19960 - 20890
        track[73] = 50;		// 20890 - 20940 v 2d4


        track[0] = 250;

        android.support.v4.app.NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setSmallIcon(R.mipmap.semiaberto)
                        .setContentTitle("Semiaberto")
                        .setContentText("Mete o pé!")
                        .setVibrate(track)
                        .setFullScreenIntent(pendingIntent,true);

        if(intent.getBooleanExtra("sonoro",false)) {
            builder.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/raw/lere"));
        }

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }
}