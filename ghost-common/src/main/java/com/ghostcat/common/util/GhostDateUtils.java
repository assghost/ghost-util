package com.ghostcat.common.util;

import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author AssGhost
 */
public class GhostDateUtils {

    private static String DEFAULT_PATTERN = "yyyy-MM-dd";

    private static DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_PATTERN);

    public static DateTimeFormatter getDefaultDateTimeFormatter() {
        return DEFAULT_DATE_TIME_FORMATTER;
    }

    public static void setDefaultDateTimeFormatter(DateTimeFormatter defaultDateTimeFormatter) {
        GhostDateUtils.DEFAULT_DATE_TIME_FORMATTER = defaultDateTimeFormatter;
    }

    public static String date2Str(LocalDate date) {
        return date2Str(date, DEFAULT_DATE_TIME_FORMATTER);
    }

    public static String date2Str(LocalDate date, String pattern) {
        return date2Str(date, DateTimeFormatter.ofPattern(pattern));
    }

    public static String date2Str(LocalDate date, DateTimeFormatter dateTimeFormatter) {
        if (null != date) {
            return date.format(dateTimeFormatter);
        }
        return "";
    }

    public static LocalDate str2Date(String dateStr) {
        return str2Date(dateStr, DEFAULT_DATE_TIME_FORMATTER);
    }

    public static LocalDate str2Date(String dateStr, String pattern) {
        return str2Date(dateStr, DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDate str2Date(String dateStr, DateTimeFormatter dateTImeFormatter) {
        if (!StringUtils.isEmpty(dateStr)) {
            return LocalDate.parse(dateStr, dateTImeFormatter);
        }

        return null;
    }

    public static Date parseDate(String dateStr, String pattern) throws ParseException {

        if (!StringUtils.isEmpty(dateStr)) {
            return new SimpleDateFormat(pattern).parse(dateStr);
        }

        return null;
    }

    public static String formatDate(Date date, String pattern) {

        if (null != date) {
            return new SimpleDateFormat(pattern).format(date);
        }

        return "";
    }

    public static Date localDateTime2Date(LocalDate localDate) {
        ZoneId zoneId = ZoneId.systemDefault();
        //Combines this date-time with a time-zone to create a  ZonedDateTime.
        ZonedDateTime zdt = localDate.atStartOfDay().atZone(zoneId);
        Date date = Date.from(zdt.toInstant());

        return date;
    }

    public static LocalDate date2LocalDateTime(Date date){
        //An instantaneous point on the time-line.(时间线上的一个瞬时点。)
        Instant instant = date.toInstant();
        //A time-zone ID, such as {@code Europe/Paris}.(时区)
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localDate = instant.atZone(zoneId).toLocalDate();

        return localDate;
    }
}