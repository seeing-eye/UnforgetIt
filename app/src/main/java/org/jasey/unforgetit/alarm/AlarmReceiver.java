package org.jasey.unforgetit.alarm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import org.jasey.unforgetit.R;
import org.jasey.unforgetit.UnforgetItActivity;
import org.jasey.unforgetit.entity.Task;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AlarmReceiver extends WakefulBroadcastReceiver {
    public static final String TITLE = "title";
    public static final String ID = "id";
    public static final String MIN_BEFORE_ALARM = "min before alarm";
    public static final String DATE = "date";

    private Map<Integer, PendingIntent> intentMap;

    public AlarmReceiver() {
        super();
        intentMap = new ConcurrentHashMap<>();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra(TITLE);
        int id = intent.getIntExtra(ID, 0);

        Intent service = new Intent(context, UnforgetItActivity.class);

        if (UnforgetItActivity.sActivityVisible) {
            service = intent;
        }

        service.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(context, id, service, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(title)
                .setSmallIcon(R.drawable.hight_priority)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(title))
                .build();

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Log.d(this.getClass().getName(), "Send " + notification.toString());

        notificationManager.notify(id, notification);
    }

    public void setAlarm(Context context, Task task, int minutes) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(TITLE, task.getTitle());
        intent.putExtra(ID, task.getId().intValue());
        intent.putExtra(MIN_BEFORE_ALARM, minutes);
        intent.putExtra(DATE, task.getDate().getTime());

        Log.d(this.getClass().getName(), "Init " + minutes + "min alarm for " + task);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, task.getId().intValue(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, task.getDate().getTime() - minutes * 60000, alarmIntent);

        intentMap.put(task.getId().intValue(), alarmIntent);

        setComponentEnabledSetting(context);
    }

    public void setAlarm(Context context, Intent intent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        String title = intent.getStringExtra(TITLE);
        int id = (int) intent.getLongExtra(ID, 0);
        int minutes = intent.getIntExtra(MIN_BEFORE_ALARM, 0);
        Long date = intent.getLongExtra(DATE, 0);

        Log.d(this.getClass().getName(), "Reset " + minutes + "min alarm for task " + title);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, date - minutes * 60000, alarmIntent);

        intentMap.put(id, alarmIntent);

        setComponentEnabledSetting(context);
    }

    private void setComponentEnabledSetting(Context context) {
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void cancelAlarm(Context context, Task task) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = intentMap.get(task.getId().intValue());
        if (alarmIntent != null) {
            Log.d(this.getClass().getName(), "Cancel " + alarmIntent);
            alarmManager.cancel(alarmIntent);
        }
    }
}
