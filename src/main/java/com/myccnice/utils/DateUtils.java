package com.myccnice.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间工具类
 *
 * create in 2017年10月19日
 * @author wangpeng
 */
public class DateUtils {

    /** UTC时间格式化器 */
    public static final SimpleDateFormat UTC_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    static {
        UTC_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    /**
     * 把日期解析为UTC时间
     */
    public static String formatUTCDate(Date ...dates) {
        Date date = new Date();
        if (dates != null && dates.length > 0) {
            date = dates[0];
        }
        return UTC_FORMAT.format(date);
    }
}
