package org.jasey.unforgetit.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

    private AlarmReceiver mAlarmReceiver = new AlarmReceiver();
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            mAlarmReceiver.onBoot(context);
        }
    }
}