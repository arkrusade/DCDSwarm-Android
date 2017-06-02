package com.orctech.dcdswarm.Helpers;

import android.content.Context;

import com.orctech.dcdswarm.Models.Block;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;



public class BlockHelper {
    public static ArrayList<Block> processBlocks(Context context) {
        //        try {
        InputStream is;
        try {
            is = context.getAssets().open("goal4.json");
            int size = is.available();
    
            byte[] buffer = new byte[size];
    
            is.read(buffer);
    
            is.close();
    
            String json = new String(buffer, "UTF-8");
            json.replace("3","3");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Block> schedule = new ArrayList<>();
        schedule.add(new Block("name", "times"));
        return schedule;
        //            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
        //            outputStream.writeObject(day.toString());
        //            outputStream.flush();
        //            outputStream.close();
        //        } catch (Exception e) {
        //            e.printStackTrace();
        //        }
          /*
                             odd i is block numbers
                             eve i is times
                             */
//
//        var blocksKey = ""
//        var timesKey = ""
//        var daySchedule: DaySchedule
//        var block: Block
//
//        let field = "FIELD"
//
//        for i in 1...fields.keys.count/2 {
//            daySchedule = DaySchedule()
//            blocksKey = field + "\(2*i-1)"
//            timesKey = field + "\(2*i)"
//
//            if let blocks = fields[blocksKey] as? [String] {
//                if let times = fields[timesKey] as? [String] {
//                    let excelDateString: String = blocks[0]
//                    if let excelNum = Int(excelDateString) {
//                        daySchedule.date = Date.fromExcelDate(excelNum)
//                    }
//
//                    for j in 1..<blocks.count {
//                        block = Block(name: blocks[j], time: times[j])
//                        if block.name == "Note" && block.time == "" {
//                            continue
//                        }
//                        if (block.name != "" || block.time != "")
//                        {
//                            daySchedule.blocks.append(block)
//                        }
//                    }
//                }
//            }
//
//            fullSchedule.append(daySchedule)
    }
}
