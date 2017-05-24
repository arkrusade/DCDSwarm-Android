package com.example.justinjlee99.dcdswarm;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void changeDateTest() throws Exception {
        Date newDate = new Date();
        assertNotEquals(newDate, DateExtension.getDateExtension().tomorrow(newDate));
        assertNotEquals(newDate, DateExtension.getDateExtension().yesterday(newDate));
        assertEquals(newDate, (DateExtension.getDateExtension().changeDate(newDate,0)));
    }
}