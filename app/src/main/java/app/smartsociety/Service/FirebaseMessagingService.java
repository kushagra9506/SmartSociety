package app.smartsociety.Service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.RemoteMessage;

import java.net.URI;
import java.util.Map;

import app.smartsociety.Common.Common;
import app.smartsociety.Dashboard.Dashboard;
import app.smartsociety.R;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    public static final String TAG = "FirebaseMessaging";

    public FirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        if (remoteMessage.getData().size() >0) {

                Log.d("Data",remoteMessage.getData().get("title"));
        }
        if (remoteMessage.getNotification() != null){
            String title = remoteMessage.getNotification().getTitle();
            String message = remoteMessage.getNotification().getBody();

            sendNotification(title,message);
        }

    }



    private void sendNotification(String title, String message) {
        long[] v = {1000, 1000, 1000, 1000, 1000};
        Uri sound = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.sound);
//        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), sound);
////        r.play();
        AudioAttributes att = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){

            String chanelid= "SmartSociety";

            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(chanelid,
                    "SmartSociety",NotificationManager.IMPORTANCE_MAX);
            notificationChannel.setDescription("Smart Society channel");
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableLights(true);
            notificationChannel.setVibrationPattern(v);
            notificationChannel.enableVibration(true);
            notificationChannel.setSound(sound,att);

            notificationManager.createNotificationChannel(notificationChannel);
        }



        Intent intent = new Intent(this, Dashboard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);



        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(message)
                .setSound(sound)
                .setContentIntent(pendingIntent)
                .setVibrate(v);



        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());



    }



    @Override
    public void onDeletedMessages() {

    }


}
