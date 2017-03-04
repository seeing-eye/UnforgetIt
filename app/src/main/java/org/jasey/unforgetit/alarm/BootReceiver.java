package org.jasey.unforgetit.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    public static final String INTENT_ACTION_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";
    private AlarmReceiver mAlarmReceiver = new AlarmReceiver();
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(INTENT_ACTION_BOOT_COMPLETED)) {
            mAlarmReceiver.onBoot(context);
        }
    }
}