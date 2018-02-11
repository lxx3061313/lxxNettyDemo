package com.lxx.mydemo.nettydemo.service.common.util.pojo;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

/**
 * 请务必使用root.lang中的JsonUtil
 * 表示只有年月日的时间 为了提高效率，使用26个位来存储 无时区 14[yyyy] + 6[mm] + 6[dd]
 *
 * @author yushen.ma
 * @version 1.0 2014-01-07 13:13:17
 */
final public class ShortDate implements Serializable, Comparable<ShortDate> {

    private static final long serialVersionUID = 4740133722012154374L;

    private int bits;

    // used by hessian2 or jackson
    @Deprecated
    public ShortDate() {}

    /** 使用yyyy-MM-dd格式字符串初始化 */
    public static ShortDate create(String str) {
        Preconditions.checkArgument(StringUtils.isNotBlank(str));
        if (str.length() != 10)
            throw new IllegalArgumentException("ShortDate format must be yyyy-MM-dd: " + str);
        int year = Integer.parseInt(str.substring(0, 4));
        int month = Integer.parseInt(str.substring(5, 7));
        int day = Integer.parseInt(str.substring(8, 10));
        return new ShortDate(year, month, day);
    }

    /** 使用yyyyMMdd格式初始化 */
    public static ShortDate createyyyyMMdd(String str) {
        Preconditions.checkArgument(StringUtils.isNotBlank(str));
        if (str.length() != 8)
            throw new IllegalArgumentException("ShortDate format must be yyyy-MM-dd: " + str);
        int year = Integer.parseInt(str.substring(0, 4));
        int month = Integer.parseInt(str.substring(4, 6));
        int day = Integer.parseInt(str.substring(6, 8));
        return new ShortDate(year, month, day);
    }

    /**
     * 使用日期对象初始化 注意这里使用了默认时区，如果非默认时区，请调用年月日显示初始化 不建议使用Date
     * 
     * @see ShortDate#create(DateTime)
     **/
    @Deprecated
    public static ShortDate create(Date date) {
        Preconditions.checkNotNull(date);
        int year = date.getYear() + 1900;
        int month = date.getMonth() + 1;
        int day = date.getDate();
        return new ShortDate(year, month, day);
    }

    public static ShortDate create(DateTime dateTime) {
        int year = dateTime.getYear();
        int month = dateTime.getMonthOfYear();
        int day = dateTime.getDayOfMonth();
        return new ShortDate(year, month, day);
    }

    /** 使用年月日初始化 */
    public static ShortDate create(int year, int month, int day) {
        return new ShortDate(year, month, day);
    }

    public static ShortDate create(ShortDate target) {
        return new ShortDate(target.bits);
    }

    private ShortDate(int year, int month, int day) {
        check(year, 0, 9999, "year is invalid");
        check(month, 1, 12, "month is invalid");
        check(day, 1, 31, "day is invalid");

        int tmp = year;
        tmp <<= 6;
        tmp |= month;
        tmp <<= 6;
        tmp |= day;

        bits = tmp;
    }

    private ShortDate(int bits) {
        this.bits = bits;
    }

    public int getYear() {
        return (bits >> 12) & 0x3FFF;
    }

    public int getMonth() {
        return (bits >> 6) & 0x3F;
    }

    public int getDay() {
        return bits & 0x3F;
    }

    /**
     * 最好使用joda-time
     * 
     * @see ShortDate#toDateTime()
     */
    @Deprecated
    public Date toDate() {
        return new Date(getYear() - 1900, getMonth() - 1, getDay());
    }

    public DateTime toDateTime() {
        return new DateTime(this.getYear(), this.getMonth(), this.getDay(), 0, 0);
    }

    public WeekDay toWeekDay() {
        return WeekDay.of(this);
    }

    public ShortDate minusDays(int days) {
        DateTime dateTime = this.toDateTime();
        return ShortDate.create(dateTime.minusDays(days));
    }

    public ShortDate minusMonths(int months) {
        DateTime dateTime = this.toDateTime();
        return ShortDate.create(dateTime.minusMonths(months));
    }

    public ShortDate minusYears(int years) {
        DateTime dateTime = this.toDateTime();
        return ShortDate.create(dateTime.minusYears(years));
    }

    public ShortDate addDays(int days) {
        DateTime dateTime = this.toDateTime();
        return ShortDate.create(dateTime.plusDays(days));
    }

    public ShortDate addMonths(int months) {
        DateTime dateTime = this.toDateTime();
        return ShortDate.create(dateTime.plusMonths(months));
    }

    public ShortDate addWeeks(int weeks) {
        DateTime dateTime = this.toDateTime();
        return ShortDate.create(dateTime.plusWeeks(weeks));
    }

    public ShortDate addYears(int years) {
        DateTime dateTime = this.toDateTime();
        return ShortDate.create(dateTime.plusYears(years));
    }

    public ShortDate min(ShortDate target) {
        return this.compareTo(target) < 0 ? this : target;
    }

    public ShortDate max(ShortDate target) {
        return this.compareTo(target) > 0 ? this : target;
    }

    public static ShortDate today() {
        return ShortDate.create(new DateTime());
    }

    public static ShortDate yesterday() {
        return ShortDate.create(new DateTime()).addDays(-1);
    }

    public static boolean isToday(ShortDate target) {
        return ShortDate.today().compareTo(target) == 0;
    }
    @Override
    public int hashCode() {
        return bits;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ShortDate other = (ShortDate) obj;
        if (bits != other.bits)
            return false;
        return true;
    }

    public String toyyyyMMdd() {
        return this.getStandardYear() + fill(getMonth()) +fill(getDay());
    }

    @Override
    public String toString() {
        return this.getStandardYear() + "-" + fill(getMonth()) + "-" + fill(getDay());
    }

    private String getStandardYear() {
        int year = getYear();
        String yearStr;
        if (year < 10) {
            yearStr = "000" + year;
        } else if (year < 100) {
            yearStr = "00" + year;
        } else if (year < 1000) {
            yearStr = "0" + year;
        } else {
            yearStr = String.valueOf(year);
        }
        return yearStr;
    }

    private static String fill(int i) {
        if (i < 10)
            return "0" + i;
        else
            return Integer.toString(i, 10);
    }

    public boolean after(ShortDate o) {
        return this.toLiteral() > o.toLiteral();
    }

    public boolean before(ShortDate o) {
        return this.toLiteral() < o.toLiteral();
    }

    @Override
    public int compareTo(ShortDate o) {
        int thisVal = this.toLiteral();
        int anotherVal = o.toLiteral();
        return (thisVal < anotherVal ? -1 : (thisVal == anotherVal ? 0 : 1));
    }

    private int toLiteral() {
        return getYear() * 10000 + getMonth() * 100 + getDay();
    }

    private static void check(int value, int min, int max, String msg) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(String.format("%s: value=%d min=%d max=%d", msg, value, min, max));
        }
    }

}
