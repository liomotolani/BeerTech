package com.beerkhaton.mealtrackerapi.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static Date subtractDays(Date date, int days){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, -days);
        return c.getTime();
    }

    public static Date dateFullFormat(String date) {
        Date date1 = null;
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        try{
            date1 = format.parse(date);
        }catch (ParseException e){
            e.printStackTrace();
        }

        return date1;
    }
}
