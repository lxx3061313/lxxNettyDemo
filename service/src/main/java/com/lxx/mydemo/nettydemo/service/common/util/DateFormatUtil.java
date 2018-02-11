package com.lxx.mydemo.nettydemo.service.common.util;

/**
 * @author yushen.ma
 * @version 2016-07-03
 */

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.collections.map.LRUMap;

/**
 * @author binyu.zhan created on 13-11-1 下午3:44
 * @version $Id$
 */
public class DateFormatUtil {
    private static final int MAX_SDF_SIZE = 30;
    private static final ThreadLocal<LRUMap> dateFormatThreadLocal = new ThreadLocal<LRUMap>() {
        @Override
        protected LRUMap initialValue() {
            return new LRUMap(MAX_SDF_SIZE);
        }
    };

    public static SimpleDateFormat getDateFormat(String pattern) {
        LRUMap sdfMap = dateFormatThreadLocal.get();
        SimpleDateFormat sdf = (SimpleDateFormat) sdfMap.get(pattern);
        if (sdf == null) {
            sdf = new SimpleDateFormat(pattern);
            sdf.setLenient(false);
            sdfMap.put(pattern, sdf);
        }
        return sdf;
    }

    public static void destory() {
        dateFormatThreadLocal.remove();
    }

    public static Date parse4y2M2d(String source) {
        try {
            return getDateFormat("yyyy-MM-dd").parse(source);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parseyyyyMMdd(String source) {
        try {
            return getDateFormat("yyyyMMdd").parse(source);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parseyyyyMMddHHmm(String source) {
        try {
            return getDateFormat("yyyyMMddHHmm").parse(source);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parseyyyyMMddHHmmss(String source) {
        try {
            return getDateFormat("yyyyMMddHHmmss").parse(source);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parse4y2M2d2h2m2s(String source) {
        try {
            return getDateFormat("yyyy-MM-dd HH:mm:ss").parse(source);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parse4y2M2d2h2m(String source) {
        try {
            return getDateFormat("yyyy-MM-dd HH:mm").parse(source);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parse4y2M2dT2h2m2s(String source) {
        try {
            return getDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(source);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parse4y2M2dhhmm(String source) {
        try {
            return getDateFormat("yyyy-MM-dd HHmm").parse(source);
        } catch (Exception e) {
            return null;
        }
    }

    public static String formatFree(Date date, String pattern) {
        return getDateFormat(pattern).format(date);
    }

    public static Date parseFree(String source,String pattern){
        try {
            return getDateFormat(pattern).parse(source);
        }catch (Exception e){
            return null;
        }
    }

    public static String format4y2M2d(Date date) {
        return getDateFormat("yyyy-MM-dd").format(date);
    }

    public static String format4y2M2dCN(Date date) {
        return getDateFormat("yyyy年MM月dd日").format(date);
    }

    public static String format2M2dCN(Date date) {
        return getDateFormat("MM月dd日").format(date);
    }

    public static String formatMdCN(Date date) {
        return getDateFormat("M月d日").format(date);
    }

    public static String formatyyyyMMdd(Date date) {
        return getDateFormat("yyyyMMdd").format(date);
    }

    public static String parseyyyyMMddHHmmss(Date date) {
        return getDateFormat("yyyyMMddHHmmss").format(date);
    }

    public static String format4y2M2d2h2m2s(Date date) {
        return getDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static String formatyyyyMMddHHmmss(Date date) {
        return getDateFormat("yyyyMMddHHmmss").format(date);
    }

    public static String format4y2M2d2h2m(Date date) {
        return getDateFormat("yyyy-MM-dd HH:mm").format(date);
    }

    public static String format2M2d2h2m(Date date) {
        return getDateFormat("MM-dd HH:mm").format(date);
    }

    public static String format2h2m2s(Date date) {
        return getDateFormat("HH:mm:ss").format(date);
    }

    public static String format2h2m(Date date) {
        return getDateFormat("HH:mm").format(date);
    }

    public static String formathhmm(Date date) {
        return getDateFormat("HHmm").format(date);
    }

    private static Calendar getCalendar(String pattern) {
        LRUMap sdfMap = dateFormatThreadLocal.get();
        if (sdfMap.containsKey(pattern)) {
            return (Calendar) sdfMap.get(pattern);
        } else {
            Calendar cal = Calendar.getInstance();
            sdfMap.put(pattern, cal);
            return cal;
        }
    }

    public static Date parseLong2Date(long longTime) {
        Calendar cal = getCalendar("long2Date");
        cal.clear();
        cal.setTimeInMillis(longTime);
        return cal.getTime();
    }

    public static Date parseHHmm(String source) {
        try {
            return getDateFormat("HH:mm").parse(source);
        } catch (Exception e) {
            return null;
        }
    }

}
