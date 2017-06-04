package com.orctech.dcdswarm.Models;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Justin Lee on 6/2/2017.
 */

public class BlockSchedule {
    private Date             date;
    private ArrayList<Block> blocks;
    
    
    public BlockSchedule(Date date) {
        this.date = date;
    }
    
    public void setBlocks(ArrayList<Block> blocks) {
        this.blocks = blocks;
    }
    
    public BlockSchedule(Date date, ArrayList<Block> blocks) {
        this.date = date;
        this.blocks = blocks;
    }
    
    public BlockSchedule(Date date, String blocks) {
        this(date);
        blocksFromString(blocks);
    }
    
    public String blocksToString() {
        String a = "[";
        for (Block b :
                blocks) {
            a += "{" + b.toString() + "}";
        }
        return a + "]";
    }
    
    private void blocksFromString(String s) {
        ArrayList<Block> blocksTemp = new ArrayList<>();
        String[] sep = s.replace("}", "").replace("[", "").replace("]", "").split("\\{");
        for (String blockStr :
                sep) {
            if (!blockStr.equals("")) {
                blocksTemp.add(new Block(blockStr));
            }
        }
        blocks = blocksTemp;
    }
    
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public ArrayList<Block> getBlocks() {
        return blocks;
    }
    
    public static BlockSchedule emptySchedule(Date date) {
        ArrayList<Block> empty = new ArrayList<>();
        empty.add(new Block("Blocks not", "found"));
        return new BlockSchedule(date, empty);
    }
}
