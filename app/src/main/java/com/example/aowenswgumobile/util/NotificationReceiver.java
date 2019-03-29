package com.example.aowenswgumobile.util;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import static com.example.aowenswgumobile.MainActivity.notificationCount;

public class NotificationReceiver extends BroadcastReceiver {

  private Bundle extras;
  @Override
  public void onReceive(Context context, Intent intent) {
    NotificationHelper notificationHelper = new NotificationHelper(context);
    extras = intent.getExtras();
    Notification.Builder nb =
            notificationHelper.getChannelNotification(
                    extras.getString("title"),
                    extras.getString("content"));
    notificationHelper.getManager().notify(notificationCount, nb.build());
  }
}
