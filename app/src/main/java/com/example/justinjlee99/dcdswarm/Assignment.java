package com.example.justinjlee99.dcdswarm;

/**
 * Created by justinjlee99 on 5/11/2017.
 */

public class Assignment {
    String className, title, description;

    public Assignment(String className, String title, String description) {
        this.className = className;
        this.title = title;
        this.description = description;
    }
    public Assignment(String[] items)
    {
        this(items[0],items[1],items[2]);
    }

    public String[] values() {
        return new String[] {className, title, description};
    }

    @Override
    public String toString() {
        return "Activity{" +
                "name='" + className + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
