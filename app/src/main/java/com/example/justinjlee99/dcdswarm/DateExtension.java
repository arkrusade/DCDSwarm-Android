package com.example.justinjlee99.dcdswarm;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Justin Lee on 5/23/2017.
 */

public class DateExtension {
    private static final DateExtension DATE_EXTENSION = new DateExtension();
    static DateExtension getDateExtension() {
        return DATE_EXTENSION;
    }

    private final DateFormat format = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());
    private Calendar c = Calendar.getInstance();

    String dateToString(Date date) {
        return format.format(date);
    }
    Date stringToDate(String string) throws ParseException {
        return format.parse(string);
    }

    Date changeDate(Date date, int diff) {
        c.setTime(date);
        c.add(Calendar.DATE, diff);
        return c.getTime();
    }

    Date tomorrow(Date date) {
        return changeDate(date, 1);
    }

    Date yesterday(Date date) {
        return changeDate(date, -1);
    }

}
