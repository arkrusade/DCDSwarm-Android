package com.example.justinjlee99.dcdswarm;

/**
 * Created by justinjlee99 on 5/11/2017.
 */

public class Assignment {
    String className, title, description;
    
    public Assignment() {
        this("", "", "");
    }
    
    public Assignment(String className, String title, String description) {
        this.className = className;
        this.title = title;
        this.description = description;
    }
    
    public Assignment(String[] items) {
        this(items[0], items[1], items[2]);
    }
    
    public String[] values() {
        return new String[]{className, title, description};
    }
    
    
    public Assignment(String str) {
        this();
        setClassName(StringCropper.cropExclusive(str, "name='", "'"));
        setTitle(StringCropper.cropExclusive(str, ", title='", "'"));
        setDescription(StringCropper.cropExclusive(str, ", description='", "'"));
    }
    
    public String toStorageString() {
        return "Assignment{" + "" + className + ", " + title + ", " + description + '}';
    }
    
    @Override
    public String toString() {
        return "Assignment{" + "name='" + className + '\'' + ", title='" + title + '\'' + ", description='" + description + '\'' + '}';
    }
    
    
    public String getClassName() {
        return className;
    }
    
    public void setClassName(String className) {
        this.className = className;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
}