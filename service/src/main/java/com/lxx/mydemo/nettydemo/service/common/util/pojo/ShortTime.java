package com.lxx.mydemo.nettydemo.service.common.util.pojo;


import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;


/**
 * 一天中的某个时间点
 * 无时区
 * 可以表达[00:00:00 - 47:59:59] 时间区间内的任意时间点
 * 为了提高效率，使用18个位来存储
 * 20[unused] + 6[hours]  + 6[minutes] + 6[seconds]
 * 
 * @author yushen.ma
 * @version 2016-04-04
 */
final public class ShortTime implements Serializable, Comparable<ShortTime>{

    private static final long serialVersionUID = -4554798261160788012L;

    protected int bits;

    public static ShortTime END_OF_DAY = ShortTime.create("23:59:59");

    public static ShortTime START_OF_DAY = ShortTime.create("00:00:00");

    // used by hessian2 or jackson
    @Deprecated
    public ShortTime() { }

    /** 使用hh:mm:ss格式的字符串初始化 */
    public static ShortTime create(String time) {
        Preconditions.checkArgument(StringUtils.isNotBlank(time));
        String[] p = time.split(":");
        int h, m, s;

        if (p.length == 3) {
            h = Integer.parseInt(p[0]);
            m = Integer.parseInt(p[1]);
            s = Integer.parseInt(p[2]);
        } else if (p.length == 2) {
            h = Integer.parseInt(p[0]);
            m = Integer.parseInt(p[1]);
            s = 0;
        } else {
            throw new IllegalArgumentException("time string pattern should be hh:mm:ss || hh:mm");
        }
        return create(h, m, s);
    }

    public static ShortTime create(int seconds){
        int h = seconds / 3600;
        seconds -= h * 3600;
        int m = seconds / 60;
        seconds -= m * 60;
        int s = seconds;
        return new ShortTime(h, m, s);
    }

    /** 使用具体的时分秒毫秒构建 **/
    public static ShortTime create(int hour, int min, int second) {
        Preconditions.checkArgument(hour >= 0 && hour <= 47);
        Preconditions.checkArgument(min >= 0 && min <= 60);
        Preconditions.checkArgument(second >= 0 && second <= 60);
        return new ShortTime(hour, min, second);
    }

    /**
     * 废弃, 建议使用create datetime
     * @see ShortTime#create(DateTime)
     */
    public static ShortTime create(Date date){
        return create(new DateTime(date));
    }

    public static ShortTime create(DateTime time){
        return ShortTime.create(time.getHourOfDay(), time.getMinuteOfHour(), time.getSecondOfMinute());
    }

    public static ShortTime now() {
        return ShortTime.create(new DateTime());
    }

    private ShortTime(int hours, int minutes, int seconds){
        check(hours, 0, 47, "hours is invalid");
        check(minutes, 0, 59, "minutes is invalid");
        check(seconds, 0, 59, "seconds is invalid");

        int tmp = 0;
        tmp |= hours;
        tmp <<= 6;
        tmp |= minutes;
        tmp <<= 6;
        tmp |= seconds;
        this.bits = tmp;;
    }

    /**
     * @return 返回小时数，如果为第二天以上，则返回值大于24
     */
    public int getHour(){
        return (bits >> 12) & 0x3F;
    }
    
    /**
     * @return 返回分钟数
     */
    public int getMinute(){
        return (bits >> 6) & 0x3F;
    }
    
    /**
     * @return 返回秒
     */
    public int getSecond() {
        return (bits) & 0x3F;
    }

    public ShortTime addHours(int delta){
        return ShortTime.create(toSeconds() + delta * 3600);
    }

    public ShortTime addMinutes(ShortTime shortTime, int delta){
        return ShortTime.create(toSeconds() + delta * 60);
    }

    public ShortTime addSeconds(ShortTime shortTime, int delta){
        return ShortTime.create(toSeconds() + delta);
    }

    public int diffSeconds(ShortTime target){
        return toSeconds() - target.toSeconds();
    }

    public int diffHours(ShortTime target) {
        return (toSeconds() - target.toSeconds()) / 3600;
    }

    @Override
    public int hashCode() {
        return bits;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ShortTime other = (ShortTime) obj;
        return bits == other.bits;
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d:%02d", getHour(), getMinute(), getSecond());
    }

    public String toStringWithoutColon(){
        return String.format("%02d%02d%02d", this.getHour(), this.getMinute(), this.getSecond());
    }

    public String toStringWithoutSecond(){
        return String.format("%02d:%02d", getHour(), getMinute());
    }

    @Override
    public int compareTo(ShortTime o) {
        long c = this.toSeconds() - o.toSeconds();
        return c == 0 ? 0 : (c < 0 ? -1 : 1);
    }
    
    public boolean after(ShortTime o){
        return this.toSeconds() > o.toSeconds();
    }
    
    public boolean before(ShortTime o){
        return this.toSeconds() < o.toSeconds();
    }

    private void check(int value, int min, int max, String msg){
        if( value < min || value > max ){
            throw new IllegalArgumentException(String.format("%s: value=%d min=%d max=%d", msg, value, min, max));
        }
    }

    public int toSeconds(){
        int s  = (bits) & 0x3F;
        int m  = (bits >> 6) & 0x3F;
        int h  = (bits >> 12) & 0x3F;
        return h * 3600 + m * 60 + s;
    }

    public int toLiteral(){
        int s  = (bits) & 0x3F;
        int m  = (bits >> 6) & 0x3F;
        int h  = (bits >> 12) & 0x3F;
        return h * 1000 + m * 100 + s;
    }
}
