package com.example.justinjlee99.dcdswarm;

import java.util.ArrayList;
import java.util.Date;


public class PortalDay {
    public ArrayList<Assignment> assignments;
    Date date;
    public PortalDay() {
        assignments = new ArrayList<>();
    }

    public PortalDay(ArrayList<Assignment> assignments) {
        this.assignments = assignments;
        this.date = new Date();
    }
    public PortalDay(ArrayList<Assignment> assignments, Date date) {
        this.assignments = assignments;
        this.date = date;
    }
    public PortalDay(Date date) {
        this.assignments = new ArrayList<>();
        this.date = date;
    }

    static PortalDay emptyDay() {
        ArrayList<Assignment> temp = new ArrayList<>();
        temp.add(new Assignment("", "There are no events to display", ""));
        return new PortalDay(temp);
    }

}
