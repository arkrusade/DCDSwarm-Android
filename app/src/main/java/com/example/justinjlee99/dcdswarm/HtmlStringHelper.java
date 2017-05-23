package com.example.justinjlee99.dcdswarm;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.example.justinjlee99.dcdswarm.StringCropper.crop;
import static com.example.justinjlee99.dcdswarm.StringCropper.cropEndExclusive;
import static com.example.justinjlee99.dcdswarm.StringCropper.cropExclusive;

/**
 * Created by justinjlee99 on 5/17/2017.
 */

public class HtmlStringHelper {
    static PortalDay processCalendarString(String htmlString) {



        //region empty day check
        String emptyStart = "<li class=\"listempty\">";
        String emptyEnd = "</li>";


        if (crop(htmlString, emptyStart, emptyEnd) != null) {
            return PortalDay.emptyDay();
        }
        //endregion

        //TODO: Cache for html logs

//        let log:HTMLLog = HTMLLog(date:date, log:htmlString)
//        CacheHelper.sharedInstance.addLog(log:log)

        //region find date
        String dateStart = "thisDate: {ts '";
        String dateEnd = "'} start:";
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        DateFormat dateFormatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        Date date = null;
        try {
            date = dateFormatter.parse(cropExclusive(htmlString, dateStart, dateEnd));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (date == null) {
            date = new Date();
        }
        //endregion

        PortalDay tempDay = new PortalDay(date);



        String dayStart = "<span class=\"listcap";
        String dayEnd = "</div>";//TODO: will not work on week or greater periods
        String dayString = null;
        dayString = cropExclusive(cropExclusive(htmlString, dayStart, dayEnd), ">");

        if (dayString == null) {
            return PortalDay.emptyDay();
        }


        //MARK: Activity Divider
        String activityStartString = "<ul class=\"eventobj calendar";
        String activityEndString = "<!-- end eventobj -->";


        String initActivity = cropExclusive(dayString, activityStartString, activityEndString);//Gets single activity
        //TODO: use activity and class ID
        String classID = cropEndExclusive(dayString, "\\");//for calendar id of class
        String activityID = cropExclusive(dayString, "\"event_", "\\\"");//gets activity ID

        dayString = cropExclusive(dayString, activityStartString);
        String currentActivity = initActivity;
        while (currentActivity != null) {//removes currActivity by finding next one


            String activityTitle;
            String activityClassName;
            String activityDesc;
            String activityString;

            if (currentActivity.contains("class=\"etitle\"")) {
//
//                if (currentActivity.startsWith("3659")) {
//                    activityTitle = cropExclusive(activityString, "\">", "/span");
//
//                    String tempClassString = activityTitle;
//
//                    activityTitle = cropEndExclusive(activityTitle, "(");
//                    if (activityTitle.contains("<br")) {
//                        tempClassString = cropExclusive(cropExclusive(tempClassString, "<br"), "(");
//                        activityTitle = cropEndExclusive(activityTitle, "<");
//                    }
//
//                    activityClass = (tempClassString.contains("(") &&
//                                     tempClassString.contains(")<"))
//                                    ? cropExclusive(tempClassString, "(", ")<")
//                                    : "Failed to find class";
//
//                }

                //			else if okActivity.hasPrefix("4790") {
                //				activityTitle =  activityString.cropExclusive("</span>")
                //				activityClass =  activityTitle.cropEnd(":")
                //				activityTitle =  activityTitle.cropExclusive(": ")
                //			}
//                else if (currentActivity.startsWith("3500")) {
//                    activityTitle = cropExclusive(cropExclusive(activityString, "title="), ">",
//                                                  "</span");
//                    activityTitle = cropExclusive(activityTitle, ": ");
//
//                    if (activityTitle == null) { activityTitle = "No title: code 3500"; }
//
//                    activityClass = cropEnd(activityTitle, ":");
//                    if (activityClass == null) { activityClass = "No class: code 3500"; }
//                }
//                else {
                activityString = crop(currentActivity, "etitle");

                //region Separating name and title
                //linked
                if (currentActivity.contains("title=\"Click here for event details\">")) {
                    activityTitle = cropExclusive(activityString, "title=\"Click here for event details\">", "</span>");//removes beginning crap in activity

                    String hasB = cropEndExclusive(activityTitle, "<br");//if has <br, crop. Since 'br' comes before 'a', skip a if it does
                    if (hasB != null) {
                        activityTitle = hasB;
                    } else {
                        String hasA = cropEndExclusive(activityTitle, "</a");
                        if (hasA != null)
                            activityTitle = hasA;
                    }

                }
                //not linked
                else {
                    activityTitle = cropExclusive(activityString, ">", "</span>");
                }
                if (activityTitle == null)
                    activityTitle = "No title found";
                //endregion
                //region Name and activity title individual parsing
                if(activityTitle.contains(":")) {
                    activityClassName = cropEndExclusive(activityTitle, ":");
                    activityTitle = cropExclusive(activityTitle, ":");
                }
                else {
                    activityClassName = activityTitle;
                    activityTitle = "Title not found";
                }
                //endregion
                // TODO: organize activities by class and use id


                //region Description parsing
                activityString = cropExclusive(activityString, "</span>");
                if (activityString.contains("eventnotes\">"))
                {
                    activityDesc = cropExclusive(activityString, "eventnotes\">", "</span>");
                }
                else {
                    activityDesc = "Description not found";
                }
                //endregion


                tempDay.assignments.add(new Assignment(activityClassName, activityTitle, activityDesc));

                //while loop logic
                //gets the next activity
                currentActivity = cropExclusive(dayString, activityStartString, activityEndString);
                dayString = cropExclusive(dayString, activityStartString);
            }

        }
        return tempDay;
    }
}
