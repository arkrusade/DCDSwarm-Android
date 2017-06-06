package com.orctech.dcdswarm.Models;

import com.orctech.dcdswarm.Helpers.StringCropper;

/**
 * Created by Justin Lee on 6/2/2017.
 */

public class Block /*implements Serializable*/ {
    private String name, times;
    
    public Block(String blockStr) {
        this();
        fromString(blockStr);
    }
    
    public Block() {
        name = "Unknown";
        times = "Unknown";
    }
    
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
        if(name.equals("0") || name.equals("null"))
            name = "";
        if(times.equals("0") || times.equals("null"))
            times = "";
        this.name = name;
        this.times = times;
    }
    
//    private void writeObject(ObjectOutputStream oos)
//            throws IOException {
//        // default serialization
//        oos.defaultWriteObject();
//        // write the object
//        List loc = new ArrayList();
//        loc.add(name);
//        loc.add(times);
//
//        oos.writeObject(loc);
//    }
//
//    private void readObject(ObjectInputStream ois)
//            throws ClassNotFoundException, IOException {
//        // default deserialization
//        ois.defaultReadObject();
//        List loc = (List) ois.readObject();
//        Block block = new Block(String.valueOf(loc.get(0)), String.valueOf(loc.get(1)));
//        this.name = block.name;
//        this.times = block.times;
//        // ... more code
//
//    }
    
    @Override
    public String toString() {
        return name + "," + times;
    }
    
    public void fromString(String s) {
        this.name = StringCropper.cropEndExclusive(s, ",");
        this.times = StringCropper.cropExclusive(s, ",");
    }
}
