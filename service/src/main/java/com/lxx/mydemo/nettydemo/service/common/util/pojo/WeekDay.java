package com.lxx.mydemo.nettydemo.service.common.util.pojo;

import com.google.common.collect.Sets;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Set;

/**
 * @author yushen.ma
 * @version 1.0 2016-04-08
 */
public enum WeekDay {

    /** 周一 */
    MONDAY(1, "周一"),

    /** 周二 */
    TUESDAY(2, "周二"),

    /** 周三 */
    WEDNESDAY(3, "周三"),

    /** 周四 */
    THURSDAY(4, "周四"),

    /** 周五 */
    FRIDAY(5, "周五"),

    /** 周六 */
    SATURDAY(6, "周六"),

    /** 周日 */
    SUNDAY(7, "周日");

    int code;

    String desc;

    WeekDay(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private static Set<WeekDay> ALL_WEEK_DAY;

    static {
        ALL_WEEK_DAY = Collections.unmodifiableSet(Sets.newHashSet(WeekDay.values()));
    }

    public static Set<WeekDay> all() {
        return ALL_WEEK_DAY;
    }

    // 这个函数性能比较重要.. 感觉需要重写了...
    public static WeekDay of(ShortDate someDay) {
        WeekDay[] all = values();
        return all[someDay.toDateTime().getDayOfWeek() - 1];
    }

    public static WeekDay of(int code) {
        for (WeekDay weekDay : values()){
            if (weekDay.getCode() == code){
                return weekDay;
            }
        }

        throw new IllegalArgumentException(MessageFormat.format("illegal WeekDay code:{0}", code));
    }

    public static boolean isWeekend(ShortDate shortDate) {
        WeekDay result = of(shortDate);
        return result == SATURDAY || result == SUNDAY;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
