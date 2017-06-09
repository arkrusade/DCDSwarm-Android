package com.orctech.dcdswarm1.Helpers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class DateExtension {
    private static final DateExtension DATE_EXTENSION = new DateExtension();
    
    public static DateExtension getDateExtension() {
        return DATE_EXTENSION;
    }
    
    public final DateFormat formatSlashed = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());
    public final DateFormat formatDashed  = new SimpleDateFormat("MM-dd-yy", Locale.getDefault());
    public final DateFormat formatWithDay = new SimpleDateFormat("E - MM/dd/yy", Locale.getDefault());
    private      Calendar   c             = Calendar.getInstance();
    
    
    //TODO: add buttons for these methods
    
    public Date changeDate(Date date, int diff) {
        c.setTime(date);
        c.add(Calendar.DATE, diff);
        return c.getTime();
    }
    
    public Date changeMonth(Date date, int diff) {
        c.setTime(date);
        c.add(Calendar.MONTH, diff);
        return c.getTime();
    }
    
    public Date tomorrow(Date date) {
        return changeDate(date, 1);
    }
    
    public Date yesterday(Date date) {
        return changeDate(date, -1);
    }
    
    public Date weekNext(Date date) {
        return changeDate(date, 7);
    }
    
    public Date weekPrev(Date date) {
        return changeDate(date, -7);
    }
    
    public Date monthNext(Date date) {
        return changeMonth(date, 1);
    }
    
    public Date monthPrev(Date date) {
        return changeMonth(date, -1);
    }
    
    public Date fromExcelDate(int num)
    {
        try {
            Date reference = DateExtension.getDateExtension().formatSlashed.parse("1/1/1900");
            return DateExtension.getDateExtension().changeDate(reference, num-2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
