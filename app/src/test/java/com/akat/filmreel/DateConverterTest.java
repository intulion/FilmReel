package com.akat.filmreel;

import com.akat.filmreel.data.db.DateConverter;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DateConverterTest {

    private Date date;
    private Long timestamp;

    @Before
    public void setUp() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2019, 11, 17, 0, 0, 0);
        date = calendar.getTime();
        timestamp = calendar.getTimeInMillis();
    }

    @Test
    public void toDate() {
        assertEquals(date, DateConverter.toDate(timestamp));
        assertNull(DateConverter.toDate(null));
    }

    @Test
    public void toTimestamp() {
        assertEquals(timestamp, DateConverter.toTimestamp(date));
        assertNull(DateConverter.toTimestamp(null));
    }

}