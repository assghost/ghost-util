package com.ghostcat.common.util;

import org.springframework.util.ObjectUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author AssGhost
 */
public class GhostDateTimeUtils {

    private static String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_PATTERN);

    private static SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat(DEFAULT_PATTERN);

    public static DateTimeFormatter getDefaultDateTimeFormatter() {
        return DEFAULT_DATE_TIME_FORMATTER;
    }

    public static void setDefaultDateTimeFormatter(DateTimeFormatter defaultDateTimeFormatter) {
        GhostDateTimeUtils.DEFAULT_DATE_TIME_FORMATTER = defaultDateTimeFormatter;
    }

    public static SimpleDateFormat getDefaultDateFormat() {
        return DEFAULT_DATE_FORMAT;
    }

    public static void setDefaultDateFormat(SimpleDateFormat defaultDateFormat) {
        DEFAULT_DATE_FORMAT = defaultDateFormat;
    }

    public static String date2Str(LocalDateTime date) {
        return date2Str(date, DEFAULT_DATE_TIME_FORMATTER);
    }

    public static String date2Str(LocalDateTime date, String pattern) {
        return date2Str(date, DateTimeFormatter.ofPattern(pattern));
    }

    public static String date2Str(LocalDateTime date, DateTimeFormatter dateTimeFormatter) {
        if (null != date) {
            return date.format(dateTimeFormatter);
        }
        return "";
    }

    public static LocalDateTime str2Date(String dateStr) {
        return str2Date(dateStr, DEFAULT_DATE_TIME_FORMATTER);
    }

    public static LocalDateTime str2Date(String dateStr, String pattern) {
        return str2Date(dateStr, DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDateTime str2Date(String dateStr, DateTimeFormatter dateTImeFormatter) {
        if (!ObjectUtils.isEmpty(dateStr)) {
            return LocalDateTime.parse(dateStr, dateTImeFormatter);
        }

        return null;
    }

    public static Date parseDate(String dateStr, SimpleDateFormat simpleDateFormat) throws ParseException {

        if (!ObjectUtils.isEmpty(dateStr)) {
            return simpleDateFormat.parse(dateStr);
        }

        return null;
    }

    public static Date parseDate(String dateStr, String pattern) throws ParseException {
        return parseDate(dateStr, new SimpleDateFormat(pattern));
    }

    public static Date parseDate(String dateStr) throws ParseException {
        return parseDate(dateStr, DEFAULT_DATE_FORMAT);
    }

    public static String formatDate(Date date, SimpleDateFormat simpleDateFormat) {

        if (null != date) {
            return simpleDateFormat.format(date);
        }

        return "";
    }

    public static String formatDate(Date date, String pattern) {
        return formatDate(date, new SimpleDateFormat(pattern));
    }

    public static String formatDate(Date date) {
        return formatDate(date, DEFAULT_DATE_FORMAT);
    }

    public static Date localDateTime2Date(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        //Combines this date-time with a time-zone to create a  ZonedDateTime.
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        Date date = Date.from(zdt.toInstant());

        return date;
    }

    public static LocalDateTime date2LocalDateTime(Date date){
        //An instantaneous point on the time-line.(时间线上的一个瞬时点。)
        Instant instant = date.toInstant();
        //A time-zone ID, such as {@code Europe/Paris}.(时区)
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();

        return localDateTime;
    }

}
