package com.tvd12.properties.file.testing;

import static org.testng.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.testng.annotations.Test;

import com.tvd12.properties.file.io.Dates;
import com.tvd12.test.base.BaseTest;

public class DatesTest extends BaseTest {

    @Test
    public void test1() {
        assertEquals(Dates.parseDate("2017-05-30", "yyyy-MM-dd"),
            LocalDate.of(2017, 5, 30));
        assertEquals(Dates.parseDateTime("2017-05-30T12:34:56:000"),
            LocalDateTime.of(2017, 5, 30, 12, 34, 56, 0));
    }

    @Test
    public void test2() {
        assertEquals(Dates.parse("2017-05-30T12:34:56:000").getTime() > 0, true);
    }

    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void test3() {
        Dates.parse("abcc");
    }

    @Override
    public Class<?> getTestClass() {
        return Dates.class;
    }
}
