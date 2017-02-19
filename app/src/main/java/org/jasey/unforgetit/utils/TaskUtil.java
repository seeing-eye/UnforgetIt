package org.jasey.unforgetit.utils;

import org.jasey.unforgetit.entity.Task;

import java.util.Calendar;
import java.util.Date;

public class TaskUtil {
    private static Task[] mTestArray = new Task[5];

    private static final Task TASK_1 = Task.buildTask((long) 1, "Feed the dog!", getDateOnFebruary2017(19, 23, 0), Task.PRIORITY_NORMAL, false);
    private static final Task TASK_2 = Task.buildTask((long) 2, "Feed the cat!", getDateOnFebruary2017(19, 6, 0), Task.PRIORITY_HIGH, true);
    private static final Task TASK_3 = Task.buildTask((long) 3, "Feed the parrot!", getDateOnFebruary2017(20, 9, 0), Task.PRIORITY_NORMAL, false);
    private static final Task TASK_4 = Task.buildTask((long) 4, "Feed fishes!", getDateOnFebruary2017(21, 16, 0), Task.PRIORITY_LOW, false);
    private static final Task TASK_5 = Task.buildTask((long) 5, "Feed somebody.", getDateOnFebruary2017(17, 9, 0), Task.PRIORITY_NORMAL, false);

    public static Task[] getTestTaskArray() {
        mTestArray[0] = TASK_1;
        mTestArray[1] = TASK_2;
        mTestArray[2] = TASK_3;
        mTestArray[3] = TASK_4;
        mTestArray[4] = TASK_5;

        return mTestArray;
    }

    public static Date getDateOnFebruary2017(int day, int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.set(2017, Calendar.FEBRUARY, day, hour, minute);
        return cal.getTime();
    }
}
