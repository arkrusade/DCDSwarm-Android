package com.orctech.dcdswarm.Helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.orctech.dcdswarm.Models.BlockSchedule;
import com.orctech.dcdswarm.Models.Login;
import com.orctech.dcdswarm.Models.PortalDay;

import java.text.DateFormat;
import java.util.Date;

public class CacheHelper {
    static final String PREFS_LOGIN_USERNAME_KEY = "__USERNAME__";
    static final String PREFS_LOGIN_PASSWORD_KEY = "__PASSWORD__";
    static final String PREFS_ASSIGNMENTS_KEY = "__ASSIGNMENTS__";
    static final String PREFS_BLOCKS_KEY = "__BLOCKS__";
    
    private static final CacheHelper instance = new CacheHelper();
    private static final DateFormat dateFormat = DateExtension.getDateExtension().formatDashed;
    
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
    public Login getLogin(Context context) {
        String username, password;
        username = getFromPrefs(context, PREFS_LOGIN_USERNAME_KEY, null);
        password = getFromPrefs(context, PREFS_LOGIN_PASSWORD_KEY, null);
        return new Login(username, password);
    }
    
    void storeLogin(Context context, String username, String password) {
        saveToPrefs(context, PREFS_LOGIN_USERNAME_KEY, username);
        saveToPrefs(context, PREFS_LOGIN_PASSWORD_KEY, password);
    }
    
    public void storeLogin(Context context, Login login) {
        storeLogin(context, login.getUsername(), login.getPassword());
    }
    //endregion
    
    //region Cache
    public void storePortalDay(Context context, PortalDay day) {
        
//        File file = new File(context.getFilesDir(), dateFormat.format(day.getDate()));
//        try {
//            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
//            outputStream.writeObject(day.toString());
//            outputStream.flush();
//            outputStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        
        saveToPrefs(context, PREFS_ASSIGNMENTS_KEY+dateFormat.format(day.getDate()), day.assignmentsToString());
    }
    
    public PortalDay getPortalDay(Context context, Date date) {
        try {
            String assignments = getFromPrefs(context, PREFS_ASSIGNMENTS_KEY+dateFormat.format(date), "");
            PortalDay day = new PortalDay(date);
            day.setAssignments(assignments);
            return day;
        } catch (Exception e) {
            e.printStackTrace();
            return PortalDay.missingDay(date);
            
        }
    }
    
    
    //endregion
    public void storeBlockSchedule(Context context, BlockSchedule schedule) {
        saveToPrefs(context, PREFS_BLOCKS_KEY+dateFormat.format(schedule.getDate()), schedule.blocksToString());
    }
    public BlockSchedule getBlockSchedule(Context context, Date date) {
        String blocks = getFromPrefs(context, PREFS_BLOCKS_KEY+dateFormat.format(date), "");
        if(blocks.equals(""))
            return BlockSchedule.emptySchedule(date);
        return new BlockSchedule(date, blocks);
    }
}

