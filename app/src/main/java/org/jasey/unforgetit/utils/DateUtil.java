package org.jasey.unforgetit.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static Date getDateOnMarch2017(int day, int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.set(2017, Calendar.MARCH, day, hour, minute);
        return cal.getTime();
    }
}
