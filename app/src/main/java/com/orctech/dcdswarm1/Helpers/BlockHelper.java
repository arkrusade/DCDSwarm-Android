package com.orctech.dcdswarm1.Helpers;

import android.content.Context;

import com.orctech.dcdswarm1.Models.Block;
import com.orctech.dcdswarm1.Models.BlockSchedule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class BlockHelper {
    /**
     * Returns the Firebase Database Reference values of the given calendar year.
     *
     * @param yearDate the given date, but only the year is used.
     * @return the string database reference values for the given calendar field.
     */
    public static String[] fileRefsForDate(Date yearDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(yearDate);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR) - 2000;
        if (month < 7) { year--; }
        String namePrefix = "/" + year + "" + (year + 1);
        namePrefix += namePrefix + "goal";
        String[] names = new String[4];
        for (int i = 0; i < 4; i++) {
            names[i] = namePrefix + (i + 1) + ".json";
        }
        return names;
    }
    //TODO: important!!
    //figure out flow from excel file to downloading dates
    //should i have first date and last date of excel file in title name?
    public static void processBlocks(String json, Context context) {
        BlockSchedule schedule;

        JSONObject goal = null;
        try {
            goal = new JSONObject(json);

          /*odd i is block numbers
            even i is times*/

            String blocksKey, timesKey;
            Block currBlock;

            final String field = "FIELD";

            for (int i = 1; i < goal.names().length() / 2; i++) {
                blocksKey = field + (2 * i - 1);
                timesKey = field + (2 * i);
                JSONArray blocks = (JSONArray) goal.get(blocksKey);
                JSONArray times = (JSONArray) goal.get(timesKey);
                int excelNum = Integer.valueOf(String.valueOf(blocks.get(0)));
                schedule = new BlockSchedule(
                        com.orctech.dcdswarm1.Helpers.DateExtension.getDateExtension()
                                                                   .fromExcelDate(excelNum));

                ArrayList<Block> blocksList = new ArrayList<>();
                blocksList.add(new Block("Block", "Times"));
                for (int j = 1; j < blocks.length(); j++) {
                    currBlock = new Block(blocks.get(j).toString(), times.get(j).toString());

                    if (!currBlock.getName().equals("") || !currBlock.getTimes().equals("")) {
                        blocksList.add(currBlock);
                    }
                }
                schedule.setBlocks(blocksList);
                CacheHelper.getInstance().storeBlockSchedule(context, schedule);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void processBlocks(Context context) {
        InputStream is;
        BlockSchedule schedule;
        try {
            is = context.getAssets().open("goal4.json");
            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            String json = new String(buffer, Charset.defaultCharset());
            JSONObject goal = new JSONObject(json);
          /*odd i is block numbers
            even i is times*/

            String blocksKey, timesKey;
            Block currBlock;

            final String field = "FIELD";

            for (int i = 1; i < goal.names().length() / 2; i++) {
                blocksKey = field + (2 * i - 1);
                timesKey = field + (2 * i);
                JSONArray blocks = (JSONArray) goal.get(blocksKey);
                JSONArray times = (JSONArray) goal.get(timesKey);
                int excelNum = Integer.valueOf(String.valueOf(blocks.get(0)));
                schedule = new BlockSchedule(
                        com.orctech.dcdswarm1.Helpers.DateExtension.getDateExtension()
                                                                   .fromExcelDate(excelNum));

                ArrayList<Block> blocksList = new ArrayList<>();
                for (int j = 1; j < blocks.length(); j++) {
                    currBlock = new Block(blocks.get(j).toString(), times.get(j).toString());

                    if (!(currBlock.getName().equals("") && currBlock.getTimes().equals(""))) {
                        blocksList.add(currBlock);
                    }
                }
                schedule.setBlocks(blocksList);
                CacheHelper.getInstance().storeBlockSchedule(context, schedule);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
