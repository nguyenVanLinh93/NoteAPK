package com.com.note.broadcastreceiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.notet.activity.AddNoteActivity;
import com.notet.activity.R;

/**
 * Created by nguyenlinh on 03/03/2017.
 * THIS IS DEMO,IT NOT WORKING
 */

public class AlarmReceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {

        long id = intent.getLongExtra("_id", 0);
        String msg = intent.getStringExtra("massage");
        String title = intent.getStringExtra("title");

        Log.d("Alarm.onReceive:", id+msg);

        PendingIntent contentIntent = setupResultActivity(context, id);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText(msg);

        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Log.d("AlarmReceiver", "onReceive: notify");
        mNotifyMgr.notify((int) id, mBuilder.build());
    }

    /**
     * setup an activity that when notification is clicked, go to the result activity
     * @param pkContext
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private PendingIntent setupResultActivity(Context pkContext, long rowId) {
        Intent resultIntent = new Intent(pkContext, AddNoteActivity.class);
        resultIntent.putExtra("id", rowId);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(pkContext);

        // Adds the back stack
        stackBuilder.addParentStack(AddNoteActivity.class);

        // Adds the Intent to the top of the stack
        stackBuilder.addNextIntent(resultIntent);

        // Gets a PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(7, PendingIntent.FLAG_UPDATE_CURRENT);

        return resultPendingIntent;
    }
}
