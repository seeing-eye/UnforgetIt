package org.jasey.unforgetit.utils;

import org.jasey.unforgetit.entity.Task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TaskUtil {
    private static List<Task> mTestList = new ArrayList<>();

    private static final Task TASK_1 = Task.buildTask((long) 1, "Feed the dog!", getDateOnFebruary2017(19, 23, 0), Task.PRIORITY_NORMAL, false);
    private static final Task TASK_2 = Task.buildTask((long) 2, "Feed the cat!", getDateOnFebruary2017(19, 6, 0), Task.PRIORITY_HIGH, true);
    private static final Task TASK_3 = Task.buildTask((long) 3, "Feed the parrot!", getDateOnFebruary2017(20, 9, 0), Task.PRIORITY_HIGH, false);
    private static final Task TASK_4 = Task.buildTask((long) 4, "Feed fishes!", getDateOnFebruary2017(21, 16, 0), Task.PRIORITY_LOW, false);
    private static final Task TASK_5 = Task.buildTask((long) 5, "Feed somebody.", getDateOnFebruary2017(17, 9, 0), Task.PRIORITY_NORMAL, false);

    public static List<Task> getTestTaskArray() {
        mTestList.add(TASK_1);
        mTestList.add(TASK_2);
        mTestList.add(TASK_3);
        mTestList.add(TASK_4);
        mTestList.add(TASK_5);

        return mTestList;
    }

    private static Date getDateOnFebruary2017(int day, int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.set(2017, Calendar.FEBRUARY, day, hour, minute);
        return cal.getTime();
    }

    public static String formatDate(Date date) {
        DateFormat myFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        return myFormat.format(date);
    }
}
