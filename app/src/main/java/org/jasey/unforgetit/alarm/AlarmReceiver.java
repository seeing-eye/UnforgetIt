package org.jasey.unforgetit.alarm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import org.jasey.unforgetit.R;
import org.jasey.unforgetit.UnforgetItActivity;
import org.jasey.unforgetit.entity.Task;
import org.jasey.unforgetit.repository.TaskRepository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AlarmReceiver extends WakefulBroadcastReceiver {
    public static final String TITLE = "title";
    public static final String ID = "id";
    public static final String ALARM_ADVANCE_TIME = "alarm advance";
    public static final String DATE = "date";
    public static final String PRIORITY = "priority";

    private Map<Integer, PendingIntent> intentMap;

    public AlarmReceiver() {
        super();
        intentMap = new ConcurrentHashMap<>();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra(TITLE);
        int id = intent.getIntExtra(ID, 0);
        int color = R.color.colorPrimary;
        int largeIcon = R.mipmap.ic_launcher;

        switch (intent.getIntExtra(PRIORITY, 0)) {
            case Task.PRIORITY_LOW:
                color = R.color.colorGreen;
                largeIcon = R.mipmap.ic_active_low;
                break;
            case Task.PRIORITY_NORMAL:
                color = R.color.colorYellow;
                largeIcon = R.mipmap.ic_active;
                break;
            case Task.PRIORITY_HIGH:
                color = R.color.colorRed;
                largeIcon = R.mipmap.ic_active_high;
                break;
        }

        Intent service = new Intent(context, UnforgetItActivity.class);

        if (UnforgetItActivity.sActivityVisible) {
            service = intent;
        }

        service.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(context, id, service, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(title)
                .setColor(ContextCompat.getColor(context, color))
                .setSmallIcon(R.drawable.ic_access_alarm_white_24dp)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), largeIcon))
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

    public void setAlarm(Context context, Task task) {
        boolean isActive = !task.isDone() &&
                            task.getAlarmAdvanceTime() != 0 &&
                            new Date(task.getDate().getTime() + task.getAlarmAdvanceTime()).after(new Date());
        if (isActive) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra(TITLE, task.getTitle());
            intent.putExtra(ID, task.getId().intValue());
            intent.putExtra(ALARM_ADVANCE_TIME, task.getAlarmAdvanceTime());
            intent.putExtra(DATE, task.getDate().getTime());
            intent.putExtra(PRIORITY, task.getPriorityLevel());

            Log.d(this.getClass().getName(), "Init " + task.getAlarmAdvanceTime() + "ms alarm for " + task);

            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, task.getId().intValue(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, task.getDate().getTime() - task.getAlarmAdvanceTime(), alarmIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, task.getDate().getTime() - task.getAlarmAdvanceTime(), alarmIntent);
            }

            intentMap.put(task.getId().intValue(), alarmIntent);

            ComponentName receiver = new ComponentName(context, AlarmReceiver.class);
            PackageManager pm = context.getPackageManager();

            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);

        }
    }

    public void onBoot(Context context) {
        List<Task> tasks = TaskRepository.getInstance(TaskRepository.Type.JPA, context).getAll();
        for (Task task : tasks) {
            setAlarm(context, task);
        }
    }

    public void cancelAlarm(Context context, Task task) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = intentMap.get(task.getId().intValue());
        if (alarmIntent != null) {
            Log.d(this.getClass().getName(), "Cancel " + alarmIntent);
            alarmManager.cancel(alarmIntent);
            intentMap.remove(alarmIntent);
        }
    }
}
