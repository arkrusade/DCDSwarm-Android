package com.orctech.dcdswarm;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Justin Lee on 5/23/2017.
 */

public class DateExtension {
    private static final DateExtension DATE_EXTENSION = new DateExtension();
    static DateExtension getInstance() {
        return DATE_EXTENSION;
    }
    
    final DateFormat formatSlashed = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());
    final DateFormat formatDashed = new SimpleDateFormat("MM-dd-yy", Locale.getDefault());
    final DateFormat formatWithDay = new SimpleDateFormat("E - MM/dd/yy", Locale.getDefault());
    private Calendar c = Calendar.getInstance();
    
//    String dateToString(Date date) {
//        return formatSlashed.formatSlashed(date);
//    }
//    Date stringToDate(String string) throws ParseException {
//        return formatSlashed.parse(string);
//    }
    
    //TODO: add buttons for these methods
//    Date weekdayNext(Date date) {
//        c.setTime(date);
//        int a = c.get(Calendar.DAY_OF_WEEK);
//        return c.getTime();
//    }
//
//    Date weekdayPrev(Date date, int diff) {
//        c.setTime(date);
//        c.add(Calendar.DATE, diff);
//        return c.getTime();
//    }
    
    Date changeDate(Date date, int diff) {
        c.setTime(date);
        c.add(Calendar.DATE, diff);
        return c.getTime();
    }
    
    Date changeMonth(Date date, int diff) {
        c.setTime(date);
        c.add(Calendar.MONTH, diff);
        return c.getTime();
    }
    
    Date tomorrow(Date date) {
        return changeDate(date, 1);
    }
    
    Date yesterday(Date date) {
        return changeDate(date, -1);
    }
    
    Date weekNext(Date date) {
        return changeDate(date, 7);
    }
    
    Date weekPrev(Date date) {
        return changeDate(date, -7);
    }
    
    Date monthNext(Date date) {
        return changeMonth(date, 1);
    }
    
    Date monthPrev(Date date) {
        return changeMonth(date, -1);
    }

}
