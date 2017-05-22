package com.example.justinjlee99.dcdswarm;

/**
 * Created by justinjlee99 on 5/11/2017.
 */

public class Assignment {
    String title,subtitle, detail;

    public Assignment(String title, String subtitle, String detail) {
        this.title = title;
        this.subtitle = subtitle;
        this.detail = detail;
    }
    public Assignment(String[] items)
    {
        this(items[0],items[1],items[2]);
    }
}
