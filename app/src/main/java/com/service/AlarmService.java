package com.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.com.note.broadcastreceiver.AlarmReceiver;

/**
 * Created by nguyenlinh on 03/03/2017.
 *
 * THIS DEMO ALARM,IT NOT WORKING
 */

public class AlarmService extends IntentService {
    public static final String CREATE = "CREATE";
    public static final String CANCEL = "CANCEL";

    private IntentFilter matcher;

    public AlarmService() {
        super("alarmService");
        Log.d("AlarmService()", "ok");
        matcher = new IntentFilter();
        matcher.addAction(CREATE);
        matcher.addAction(CANCEL);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String action = intent.getAction();

        if (matcher.matchAction(action)) {
            execute(action, intent);
        }
    }

    private void execute(String action, Intent data) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent i = new Intent(this, AlarmReceiver.class);
        i.putExtra("_id", data.getLongExtra("_id", 0));
        i.putExtra("massage", data.getStringExtra("massage"));
        i.putExtra("title", data.getStringExtra("title"));
        long time = data.getLongExtra("triggerTime", 0);

        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        if (CREATE.equals(action) && time != 0) {
            Log.d("AlarmService.execute", "ok");
            am.set(AlarmManager.RTC_WAKEUP, time, pi);

        } else if (CANCEL.equals(action)) {
            Log.d("AlarmService.execute:", "cancel");
            am.cancel(pi);
        }
    }
}
