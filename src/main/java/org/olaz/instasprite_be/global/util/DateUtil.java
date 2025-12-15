package org.olaz.instasprite_be.global.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Utility class for date formatting operations
 */
public class DateUtil {

    /**
     * Converts a Date object to a formatted string
     * Format: yyyy-MM-dd HH:mm
     * Timezone: UTC
     * 
     * @param date The date to convert
     * @return Formatted date string
     */
    public static String convertDateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        return sdf.format(date);
    }
}

