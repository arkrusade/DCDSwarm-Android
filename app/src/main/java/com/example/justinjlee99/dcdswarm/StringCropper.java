package com.example.justinjlee99.dcdswarm;

/**
 * Created by justinjlee99 on 5/16/2017.
 */

class StringCropper {
    static String crop(String input, String start) throws StringIndexOutOfBoundsException {
        int index = input.indexOf(start);
        if (index < 0 || index >= input.length()) {
            throw new StringIndexOutOfBoundsException("Start String not contained within input");
        } else return input.substring(index);
    }

    static String cropExclusive(String input, String start) throws StringIndexOutOfBoundsException {
        int index = input.indexOf(start);
        if (index < 0 || index >= input.length()) {
            throw new StringIndexOutOfBoundsException("Start String not contained within input");
        } else return input.substring(index + start.length());
    }

    static String cropEnd(String input, String end) throws StringIndexOutOfBoundsException {
        int index = input.indexOf(end);
        if (index < 0 || index >= input.length()) {
            throw new StringIndexOutOfBoundsException("End String not contained within input");
        } else return input.substring(0, index + end.length());
    }

    static String cropEndExclusive(String input, String end) throws StringIndexOutOfBoundsException {
        int index = input.indexOf(end);
        if (index < 0 || index >= input.length()) {
            throw new StringIndexOutOfBoundsException("End String not contained within input");
        } else return input.substring(0, index);
    }

    static String crop(String input, String start, String end) throws StringIndexOutOfBoundsException {
        return cropEnd(crop(input, start), end);
    }

    static String cropExclusive(String input, String start, String end) throws StringIndexOutOfBoundsException {
        return cropEndExclusive(cropExclusive(input, start), end);
    }
}
