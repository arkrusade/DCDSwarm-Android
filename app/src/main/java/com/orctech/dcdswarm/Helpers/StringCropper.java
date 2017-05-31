package com.orctech.dcdswarm.Helpers;


import android.support.annotation.Nullable;

public class StringCropper {
    public static String crop(@Nullable String input, @Nullable String start) {
        if (input == null || start == null)
            return null;
        int index = input.indexOf(start);
        if (index < 0 || index >= input.length()) {
            return null;
        } else
            return input.substring(index);
    }

    public static String cropExclusive(@Nullable String input, @Nullable String start) {
        if (input == null || start == null)
            return null;
        int index = input.indexOf(start);
        if (index < 0 || index >= input.length()) {
            return null;
        } else
            return input.substring(index + start.length());
    }

    static String cropEnd(@Nullable String input, @Nullable String end) {
        if (input == null || end == null)
            return null;
        int index = input.indexOf(end);
        if (index < 0 || index >= input.length()) {
            return null;
        } else
            return input.substring(0, index + end.length());
    }

    public static String cropEndExclusive(@Nullable String input, @Nullable String end) {
        if (input == null || end == null)
            return null;
        int index = input.indexOf(end);
        if (index < 0 || index >= input.length()) {
            return null;
        } else
            return input.substring(0, index);
    }

    public static String crop(@Nullable String input, @Nullable String start, @Nullable String end) {
        return cropEnd(crop(input, start), end);
    }

    public static String cropExclusive(@Nullable String input, @Nullable String start, @Nullable
            String end) {
        return cropEndExclusive(cropExclusive(input, start), end);
    }
}
