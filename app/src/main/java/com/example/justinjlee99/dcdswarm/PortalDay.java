package com.example.justinjlee99.dcdswarm;

import java.util.ArrayList;
import java.util.Date;


public class PortalDay {
    public ArrayList<Assignment> assignments;
    Date date;
    //TODO: make date for checking against errors, this date should not be final
    //when there is nothing, return proper date
    //maybe keep for working with longer periods like weeks
    public PortalDay() {
        assignments = new ArrayList<>();
    }

    public PortalDay(ArrayList<Assignment> assignments, Date date) {
        this.assignments = assignments;
        this.date = date;
    }
    public PortalDay(Date date) {
        this.assignments = new ArrayList<>();
        this.date = date;
    }

    static PortalDay emptyDay(Date date) {
        ArrayList<Assignment> temp = new ArrayList<>();
        temp.add(new Assignment("", "There are no events to display", ""));
        return new PortalDay(temp, date);
    }

}
