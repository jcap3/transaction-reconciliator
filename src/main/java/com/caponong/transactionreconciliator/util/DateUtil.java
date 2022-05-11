package com.caponong.transactionreconciliator.util;

import com.caponong.transactionreconciliator.error.exception.ParseDateError;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class DateUtil {

    public static final String ISO_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    private DateUtil () {}

    public static Date parseDateString(String date) {
        SimpleDateFormat dateParser = new SimpleDateFormat(ISO_DATE_TIME_FORMAT);
        try {
            return dateParser.parse(date);
        } catch (ParseException e) {
            log.error("Error parsing date");
            throw new ParseDateError(e);
        }
    }
}
