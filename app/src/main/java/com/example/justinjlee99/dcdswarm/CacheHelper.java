package com.example.justinjlee99.dcdswarm;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.util.Date;

public class CacheHelper {
    static final String PREFS_LOGIN_USERNAME_KEY = "__USERNAME__";
    static final String PREFS_LOGIN_PASSWORD_KEY = "__PASSWORD__";
    private static final CacheHelper instance = new CacheHelper();
    private static final DateFormat dateFormat = DateExtension.getInstance().formatDashed;
    
    public static CacheHelper getInstance() {
        return instance;
    }
    
    /**
     * Called to save supplied value in shared preferences against given key.
     *
     * @param context Context of caller activity
     * @param key     Key of value to save against
     * @param value   Value to save
     */
    
    private void saveToPrefs(Context context, String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }
    
    /**
     * Called to retrieve required value from shared preferences, identified by given key.
     * Default value will be returned of no value found or error occurred.
     *
     * @param context      Context of caller activity
     * @param key          Key to find value against
     * @param defaultValue Value to return if no data found against given key
     * @return Return the value found against given key, default if not found or any error occurs
     */
    private String getFromPrefs(Context context, String key, String defaultValue) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            return sharedPrefs.getString(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }
    
    //region Login
    Credentials getLogin(Context context) {
        String username, password;
        username = getFromPrefs(context, PREFS_LOGIN_USERNAME_KEY, null);
        password = getFromPrefs(context, PREFS_LOGIN_PASSWORD_KEY, null);
        return new Credentials(username, password);
    }
    
    void storeLogin(Context context, String username, String password) {
        saveToPrefs(context, PREFS_LOGIN_USERNAME_KEY, username);
        saveToPrefs(context, PREFS_LOGIN_PASSWORD_KEY, password);
    }
    
    void storeLogin(Context context, Credentials login) {
        storeLogin(context, login.username, login.password);
    }
    //endregion
    
    Context context(Context c) {
        return c;
    }
    //region Cache
    void storePortalDay(Context context, PortalDay day) {
        
        File file = new File(context.getFilesDir(), dateFormat.format(day.date));
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(day.toString());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        saveToPrefs(context, dateFormat.format(day.date), day.assignmentsToString());
    }
    
    PortalDay getPortalDay(Context context, Date date) {
        StringBuilder text = new StringBuilder();
        try {
            String assignments = getFromPrefs(context, dateFormat.format(date), "");
            PortalDay day = new PortalDay(date);
            day.setAssignments(assignments);
            return day;
        } catch (Exception e) {
            e.printStackTrace();
            return PortalDay.missingDay(date);
            
        }
//        try {
//            return new PortalDay(text.toString());
//        } catch (ParseException e) {
//            e.printStackTrace();
//            return PortalDay.missingDay(date);
//        }
        
        
    }
    //endregion
    
}

class Credentials {
    String username, password;
    
    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }
}