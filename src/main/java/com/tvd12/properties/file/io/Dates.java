package com.tvd12.properties.file.io;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public final class Dates {

    private static final DateTimeFormatter DATE_TIME_FORMATTER
            = getDateTimeFormatter(getPattern());
    
    private Dates() {}
    
    // ============= java 8 ============
    public static LocalDate parseDate(String source, String pattern) {
        return parseDate(source, getDateTimeFormatter(pattern));
    }
    
    public static LocalDate parseDate(String source, DateTimeFormatter formatter) {
        return LocalDate.parse(source, formatter);
    }
    
    public static LocalDateTime parseDateTime(String source) {
        return parseDateTime(source, getDateTimeFormatter());
    }
    
    public static LocalDateTime parseDateTime(String source, DateTimeFormatter formatter) {
        return LocalDateTime.parse(source, formatter);
    }
    
    public static DateTimeFormatter getDateTimeFormatter() {
        return DATE_TIME_FORMATTER;
    }
    
    public static DateTimeFormatter getDateTimeFormatter(String pattern) {
        return DateTimeFormatter.ofPattern(pattern);
    }
    //=================================
    
    
    // =================== java 7 ===============
    public static Date parse(String source) {
        return parse(source, getPattern());
    }
    
    public static Date parse(String source, String pattern) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            return formatter.parse(source);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }
    
    public static String getPattern() {
        return "yyyy-MM-dd'T'HH:mm:ss:SSS";
    }
}
