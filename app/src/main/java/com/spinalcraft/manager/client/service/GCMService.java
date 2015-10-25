package com.spinalcraft.manager.client.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spinalcraft.manager.client.Application;
import com.spinalcraft.manager.client.R;
import com.spinalcraft.manager.client.activity.ApplicationActivity;
import com.spinalcraft.manager.client.activity.MainActivity;

import java.lang.reflect.Type;

public class GCMService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data){
        String message = data.getString("message");
        System.out.println("From: " + from);
//        System.out.println("Message: " + message);
        if(from.equals("/topics/applications")){
            Type type = new TypeToken<Application>(){}.getType();
            Application application = new Gson().fromJson(data.getString("application"), type);

            sendNotification(application);
        }
        else{
            //non-topic message
        }
    }

    private void sendNotification(Application application) {
        Intent intent = new Intent(this, ApplicationActivity.class);
        intent.putExtra("application", application);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this)
                .addNextIntentWithParentStack(intent);

//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle("Application received")
                .setContentText(application.username)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
