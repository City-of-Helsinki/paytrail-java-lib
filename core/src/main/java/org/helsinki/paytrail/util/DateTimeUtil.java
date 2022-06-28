package org.helsinki.paytrail.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    public static String getDate() {
        return getFormattedDate(LocalDate.now());
    }

    public static String getFormattedDate(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        return getFormattedDate(localDate, formatter);
    }

    public static String getFormattedDate(String localDateString, DateTimeFormatter formatter) {
        LocalDate localDate = LocalDate.parse(localDateString);
        return getFormattedDate(localDate, formatter);
    }

    public static String getFormattedDate(LocalDate localDate, DateTimeFormatter formatter) {
        return localDate.format(formatter);
    }

    public static String getDateTime() {
        return getFormattedDateTime(LocalDateTime.now());
    }

    public static String getFormattedDateTime(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String dateWithCommonFormatting = localDateTime.format(formatter);

        return dateWithCommonFormatting;
    }

    public static LocalDateTime getFormattedDateTime() {
        return fromFormattedString(getDateTime());
    }

    public static LocalDateTime fromFormattedString(String localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return LocalDateTime.parse(localDateTime, formatter);
    }

    public static boolean isSameDay(LocalDateTime date1, LocalDateTime date2) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd");
        return fmt.format(date1).equals(fmt.format(date2));
    }

}
