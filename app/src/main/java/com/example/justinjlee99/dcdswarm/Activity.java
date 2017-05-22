package com.example.justinjlee99.dcdswarm;

/**
 * Created by justinjlee99 on 5/16/2017.
 */

public class Activity {
    String name, title, subtitle;

    public Activity(String name, String title, String subtitle) {
        this.name = name;
        this.title = title;
        this.subtitle = subtitle;
    }

    public Activity(String[] vals) {
        this(vals[0], vals[1], vals[2]);
    }

    public String[] values() {
        return new String[] { name, title, subtitle };
    }

    @Override
    public String toString() {
        return "Activity{" +
               "name='" + name + '\'' +
               ", title='" + title + '\'' +
               ", subtitle='" + subtitle + '\'' +
               '}';
    }
}
