package com.example.aowenswgumobile.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.v4.app.NotificationCompat;

import com.example.aowenswgumobile.R;

public class NotificationHelper extends ContextWrapper {

  public static final String channelID = "1";
  public static final String channelName = "notificationChannel";
  private NotificationManager mManager;

  public NotificationHelper(Context base) {
    super(base);
    createChannel();
  }

  public NotificationManager getManager() {
    if (mManager == null) {
      mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    return mManager;
  }

  private void createChannel() {
    NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);

    getManager().createNotificationChannel(channel);
  }

  public Notification.Builder getChannelNotification(String titleText, String contentText) {
    return new Notification.Builder(getApplicationContext(),channelID)
            .setContentTitle(titleText)
            .setContentText(contentText)
            .setSmallIcon(R.drawable.ic_o_icon);
  }
}
