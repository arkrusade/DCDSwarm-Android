package com.example.justinjlee99.dcdswarm;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import com.example.justinjlee99.dcdswarm.StringCropper;

/**
 * Created by justinjlee99 on 5/16/2017.
 */

public class PortalDay {
    ArrayList<Activity> list;

    public PortalDay() {
        list = new ArrayList<>();
    }
    public PortalDay(ArrayList<Activity> list) {
        this.list = list;
    }
    static PortalDay emptyDay() {
        return new PortalDay(
                (ArrayList) Arrays.asList(new Activity("", "There are no events to display", "")));
    }

}
