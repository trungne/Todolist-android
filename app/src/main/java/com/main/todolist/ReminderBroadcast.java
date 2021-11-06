package com.main.todolist;

import android.app.Application;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderBroadcast extends BroadcastReceiver {
    public static final String TASK_DESCRIPTION_KEY = "com.main.todolist.ReminderBroadcast.taskDescription";
    public static final String TASK_ID_KEY = "com.main.todolist.ReminderBroadcast.taskID";
    @Override
    public void onReceive(Context context, Intent intent) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_note);

        String description = intent.getStringExtra(TASK_DESCRIPTION_KEY);
        int id = intent.getIntExtra(TASK_ID_KEY, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_note)
                .setLargeIcon(bitmap)
                .setContentTitle("To Do List")
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(id, builder.build());
    }


}
