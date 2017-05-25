package com.example.justinjlee99.dcdswarm;

import java.util.ArrayList;
import java.util.Arrays;
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
    
    //    public PortalDay(String str) throws ParseException {
//        this(DateExtension.getInstance().formatSlashed.parse(StringCropper.cropExclusive(str, "PortalDay{date=", ", assignments={")));
//         String assignmentString = StringCropper.cropExclusive(str, ", assignments={","}");
//    }
    public void setAssignments(String assignments) {
        this.assignments = new ArrayList<>();
        String[] temp = StringCropper.cropExclusive(assignments, "assignments={[", "]}").replace("}", "").split("Assignment\\{");
        for (String a : temp) {
            if (a.contains("name") || a.contains("title") || a.contains("description"))
                this.assignments.add(new Assignment(a));
        }
        
    }
    
    public String assignmentsToString() {
        StringBuilder str = new StringBuilder("assignments={");
        str.append(Arrays.toString(assignments.toArray()));
        str.append("}");
        return str.toString();
    }
    
    @Override
    public String toString() {
        return "PortalDay{" + "date=" + DateExtension.getInstance().formatSlashed.format(date) + ", " + assignmentsToString() + '}';
    }
    
    static PortalDay emptyDay(Date date) {
        ArrayList<Assignment> temp = new ArrayList<>();
        temp.add(new Assignment("", "There are no events to display", ""));
        return new PortalDay(temp, date);
    }
    
    static PortalDay missingDay(Date date) {
        ArrayList<Assignment> temp = new ArrayList<>();
        temp.add(new Assignment("", "Data not found", ""));
        return new PortalDay(temp, date);
    }
    
    
}
