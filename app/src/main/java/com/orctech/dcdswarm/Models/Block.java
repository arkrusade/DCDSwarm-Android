package com.orctech.dcdswarm.Models;

/**
 * Created by Justin Lee on 6/2/2017.
 */

public class Block {
    String name, times;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getTimes() {
        return times;
    }
    
    public void setTimes(String times) {
        this.times = times;
    }
    
    public Block(String name, String times) {
    
        this.name = name;
        this.times = times;
    }
}
