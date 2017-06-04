package com.orctech.dcdswarm.Helpers;

import android.content.Context;

import com.orctech.dcdswarm.Models.Block;
import com.orctech.dcdswarm.Models.BlockSchedule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class BlockHelper {
    public static void processBlocks(Context context) {
        InputStream is;
        BlockSchedule schedule;
        try {
            is = context.getAssets().open("goal4.json");
            int size = is.available();
            
            byte[] buffer = new byte[size];
            
            is.read(buffer);
            
            is.close();
            
            String json = new String(buffer, "UTF-8");
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
                schedule = new BlockSchedule(DateExtension.getDateExtension().fromExcelDate(excelNum));
                
                ArrayList<Block> blocksList = new ArrayList<>();
                for (int j = 1; j < blocks.length(); j++) {
                    currBlock = new Block(blocks.get(j).toString(), times.get(j).toString());
                    
                    if (!currBlock.getName().equals("") || currBlock.getTimes().equals("")) {
                        blocksList.add(currBlock);
                    }
                }
                schedule.setBlocks(blocksList);
                CacheHelper.getInstance().storeBlockSchedule(context, schedule);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    
}
