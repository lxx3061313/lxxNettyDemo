package com.lxx.mydemo.nettydemo.service.common.util.pojo;

import java.io.Serializable;

/**
 * @author yushen.ma
 * @version 2016-06-28
 */
public class TimeRange implements Serializable {

    private ShortTime from;

    private ShortTime to;

    // use by framework
    @Deprecated
    public TimeRange() { }

    private TimeRange(ShortTime from, ShortTime to) {
        this.from = from;
        this.to = to;
    }

    public static TimeRange create(ShortTime from, ShortTime to) {
        return new TimeRange(from, to);
    }

    public ShortTime getFrom() {
        return from;
    }

    public ShortTime getTo() {
        return to;
    }

    public boolean contains(ShortTime shortTime) {
        return shortTime.compareTo(getFrom()) >= 0 && shortTime.compareTo(getTo()) <= 0;
    }

    @Override
    public String toString() {
        return "[" + from + "~" + to + "]";
    }

}
