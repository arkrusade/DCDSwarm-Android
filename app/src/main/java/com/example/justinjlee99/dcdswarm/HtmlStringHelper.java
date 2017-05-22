package com.example.justinjlee99.dcdswarm;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.example.justinjlee99.dcdswarm.StringCropper.crop;
import static com.example.justinjlee99.dcdswarm.StringCropper.cropEnd;
import static com.example.justinjlee99.dcdswarm.StringCropper.cropEndExclusive;
import static com.example.justinjlee99.dcdswarm.StringCropper.cropExclusive;

/**
 * Created by justinjlee99 on 5/17/2017.
 */

public class HtmlStringHelper {
    static PortalDay processCalendarString(String htmlString) {

        //MARK: find date of events
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

        //MARK: empty day check
        String emptyStart = "<li class=\"listempty\">";
        String emptyEnd = "</li>";


        if (crop(htmlString, emptyStart, emptyEnd) == null) {
            return PortalDay.emptyDay();
        }
        //TODO: Cache for html logs
//        let log:HTMLLog = HTMLLog(date:date, log:htmlString)
//        CacheHelper.sharedInstance.addLog(log:log)


        //MARK: PortalDay divider

        String dayStart = "<span class=\"listcap";
        String dayEnd = "</div>";//TODO: will not work on week or greater periods
        String dayString = null;
        dayString = cropExclusive(cropExclusive(htmlString, dayStart, dayEnd), ">");

        if (dayString == null) { return PortalDay.emptyDay(); }
        PortalDay tempDay = new PortalDay();


        //MARK: Activity Divider
        String activityStartString = "<ul class=\"eventobj calendar";
        String activityEndString = "<!-- end eventobj -->";


        String currentActivity = cropExclusive(dayString, activityStartString,
                                               activityEndString);//Gets single activity
        //TODO: use activity and class ID
        String classID = cropEndExclusive(dayString, "\\");//for calendar id of class
        String activityID = cropExclusive(dayString, "\"event_", "\\\"");//gets activity ID

        dayString = cropExclusive(dayString, activityStartString);
        String okActivity = currentActivity;
        while (okActivity != null) {//removes currActivity by finding next one


            String activityTitle = "Title not found";
            String activityClass = null;
            String activityDesc = null;
            String activityString = null;
            //MARK: parsing Title
            if (okActivity.contains("etitle")) {
                activityString = crop(okActivity, "etitle");
                if (okActivity.startsWith("3659")) {
                    activityTitle = cropExclusive(activityString, "\">", "/span");

                    String tempClassString = activityTitle;

                    activityTitle = cropEndExclusive(activityTitle, "(");
                    if (activityTitle.contains("<br")) {
                        tempClassString = cropExclusive(cropExclusive(tempClassString, "<br"), "(");
                        activityTitle = cropEndExclusive(activityTitle, "<");
                    }

                    activityClass = (tempClassString.contains("(") &&
                                     tempClassString.contains(")<"))
                                    ? cropExclusive(tempClassString, "(", ")<")
                                    : "Failed to find class";

                }

                //			else if okActivity.hasPrefix("4790") {
                //				activityTitle =  activityString.cropExclusive("</span>")
                //				activityClass =  activityTitle.cropEnd(":")
                //				activityTitle =  activityTitle.cropExclusive(": ")
                //			}
                else if (okActivity.startsWith("3500")) {
                    activityTitle = cropExclusive(cropExclusive(activityString, "title="), ">",
                                                  "</span");
                    activityTitle = cropExclusive(activityTitle, ": ");

                    if (activityTitle == null) { activityTitle = "No title: code 3500"; }

                    activityClass = cropEnd(activityTitle, ":");
                    if (activityClass == null) { activityClass = "No class: code 3500"; }
                }
                else {
                    //linked
                    if (okActivity.contains("title=\"Click here for event details\">")) {
                        activityTitle = cropExclusive(activityString,
                                                      "title=\"Click here for event details\">",
                                                      "</span>");//removes beginning crap in activity

                        //separates class name from activity title
                        if (activityTitle.contains("):")) {
                            activityClass = cropEnd(activityTitle, "):");
//                                cropExclusive()
//                            (at:tempClass.characters.index(before: ztempClass.endIndex))


                        }
                        else {
                            //Activity not found
                            //TODO: change to only change if not null
                            activityTitle = cropExclusive(cropExclusive(activityString, "title="),
                                                          ">", "</span");
                            if (activityTitle == null) { activityTitle = "No title: code 0"; }
                            activityClass = cropEnd(activityTitle, ":");
                            if (activityClass == null) { activityClass = "No class: code 0"; }
                            activityTitle = cropExclusive(activityTitle, ": ");
                            if (activityTitle == null) { activityTitle = "No title: code 1"; }

                            String tempTitle = cropExclusive(activityTitle, "): ", "</");
                            activityTitle = tempTitle == null
                                            ? "No title: code 1"
                                            : tempTitle;//no colon

                            if (activityTitle.contains("<br")) {
                                activityTitle = cropEndExclusive(activityTitle, "<br");
                            }
                        }
                    }
                    //not linked
                    else if (okActivity.contains("<span class=\"eventcon\">")) {
                        //find event content
                        String tempActivity = cropExclusive(okActivity, "id=\"e_");
                        if (tempActivity != null) {
//                            String activityID = cropEndExclusive(tempActivity,"\">");
                            //TODO: organize activities by class and use id, and what is the
                            // above thing
                            activityClass = cropExclusive(tempActivity, "\">", "): ") + ")";
                            activityTitle = cropExclusive(tempActivity, "): ", "</span>");


                            if (activityTitle.contains("<br")) {
                                activityTitle = cropEndExclusive(activityTitle, "<br");
                            }
                        }
                        else {
                            activityTitle = "Failed title: code eventcon";
                        }
                    }
                    else {
                        activityClass = "Could not find activity";
                    }
                }

                //MARK: parsing Desc
                String activityDescData = cropExclusive(activityString, "</span>");
                if (activityDescData != null) {
                    activityDesc = "";
                    while (activityDescData.contains("<span")) {
                        activityDescData = cropExclusive(activityDescData, "<span");
                        activityDescData = cropExclusive(activityDescData, ">", "</span");

                        //replace <br> with \n
                        if (activityDescData.contains("<") &&
                            activityDescData.contains(">")) {//if carats found
                            //find carats and make range
                            int startIndex = activityDescData.indexOf("<");
                            int endIndex = activityDescData.indexOf(">");
                            //make sure 'br' is between carats
                            if (activityDescData.substring(startIndex, endIndex).contains("br") ||
                                activityDescData.substring(startIndex, endIndex).contains
                                        ("BR")) {
                                activityDescData = activityDescData.substring(0, startIndex) +
                                                   "\n" +
                                                   activityDescData.substring(endIndex);
                            }
                            else {
                                System.out.println("carats found, but no break");
                                break;
                            }
                        }
                        activityDesc += (activityDescData) + "\n";
                    }
                }

                tempDay.list.add(new Activity(activityClass, activityTitle, activityDesc));

                //while loop logic
                //gets the next activity
                currentActivity = cropExclusive(dayString, activityStartString, activityEndString);
                dayString = cropExclusive(dayString, activityStartString);
            }

        }
        return tempDay;
    }
}
