package com.orctech.dcdswarm.Helpers;

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
    
    public static DateExtension getInstance() {
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
    
    Date changeMonth(Date date, int diff) {
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
    
    Date monthNext(Date date) {
        return changeMonth(date, 1);
    }
    
    Date monthPrev(Date date) {
        return changeMonth(date, -1);
    }
    
}
