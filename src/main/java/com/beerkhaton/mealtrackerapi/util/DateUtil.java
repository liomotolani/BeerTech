package com.beerkhaton.mealtrackerapi.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static Date subtractDays(Date date, int days){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, -days);
        return c.getTime();
    }
}
