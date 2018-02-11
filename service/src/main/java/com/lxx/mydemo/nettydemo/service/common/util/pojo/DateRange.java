package com.lxx.mydemo.nettydemo.service.common.util.pojo;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.Days;

/**
 * 如果要序列化该对象,
 * 请务必使用root.lang中的JsonUtil
 *
 * @author yushen.ma
 * @version 1.0 2016-04-08
 */
final public class DateRange implements Iterable<ShortDate>,
        Serializable, Comparable<DateRange> {

    private static final long serialVersionUID = 6594355815387267223L;

    private ShortDate fromDate;

    private ShortDate toDate;

    private static Splitter splitter = Splitter.on(",");

    // use by hessian2 or jackson
    @Deprecated
    public DateRange() {}

    public static DateRange create(int days) {
        return DateRange.create(ShortDate.today(), ShortDate.today().addDays(days));
    }

    public static DateRange create(String json) {
        Preconditions.checkArgument(StringUtils.isNotBlank(json));
        json = json.replace("[", "");
        json = json.replace("]", "");
        List<String> strings = splitter.splitToList(json);
        Preconditions.checkArgument(CollectionUtils.size(strings) == 2);
        ShortDate from = ShortDate.create(StringUtils.trimToEmpty(strings.get(0)));
        ShortDate to = ShortDate.create(StringUtils.trimToEmpty(strings.get(1)));
        return create(from, to);
    }

    public static DateRange create(ShortDate fromDate, ShortDate toDate) {
        return new DateRange(fromDate, toDate);
    }

    public static DateRange create(ShortDate fromDate) {
        return new DateRange(fromDate, fromDate.addDays(1));
    }

    private DateRange(ShortDate fromDate, ShortDate toDate) {
        Preconditions.checkArgument(fromDate != null && toDate != null
                && (fromDate.compareTo(toDate) < 0),
                "invalid param [%s-%s]", fromDate, toDate);
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public DateRange add(ShortDate shortDate) {
        DateRange shortDates = DateRange.create(shortDate);
        return this.add(shortDates);
    }

    public DateRange add(DateRange target) {
        Preconditions.checkArgument(this.isConnected(target),
                "this.[%s]和that[%s]根本就连不上", this, target);
        ShortDate from = fromDate.min(target.getFromDate());
        ShortDate to = toDate.max(target.getToDate());
        return DateRange.create(from, to);
    }

    /**
     * @param start 开始的index, 从头部开始数
     * @param end 结束的index, 从尾部开始数
     * @return DateRange
     */
    public DateRange sub(int start, int end) {
        Preconditions.checkArgument(start >= 0);
        Preconditions.checkArgument(end >= 0);
        ShortDate from = fromDate.addDays(start);
        ShortDate to = toDate.minusDays(end);
        Preconditions.checkArgument(from.compareTo(to) < 0, "this.[%s],sub序列越界", this);
        return DateRange.create(from, to);
    }

    public DateRange sub(int start) {
        return sub(start, 0);
    }

    /**
     * connect 上不代表有交集
     * 例如 : [2015-01-15 - 2015-01-20 ] [2015-01-20] - [2015-01-25]
     */
    public boolean isConnected(DateRange dates) {
        return !isNotConnected(dates);
    }

    public boolean isConnected(ShortDate shortDate) {
        return isConnected(DateRange.create(shortDate));
    }

    public boolean isNotConnected(DateRange dates) {
        return this.fromDate.compareTo(dates.toDate) > 0 || this.toDate.compareTo(dates.fromDate) < 0;
    }

    public boolean hasIntersection(DateRange target) {
        if (!isConnected(target)) {
            return false;
        }
        // 排除只是恰好链接上的情况
        if (this.fromDate.equals(target.toDate)
                || this.toDate.equals(target.fromDate)) {
            return false;
        }
        //
        return true;
    }

    /** 取交集 */
    public DateRange intersection(DateRange target) {
        Preconditions.checkArgument(hasIntersection(target));
        ShortDate from = this.fromDate.max(target.fromDate);
        ShortDate to = this.toDate.min(target.toDate);
        return DateRange.create(from, to);
    }

    /** 是否只有一天 */
    public boolean isSingleDay() {
        return fromDate.addDays(1).compareTo(toDate) == 0;
    }

    public ShortDate getFromDate() {
        return fromDate;
    }

    public ShortDate getToDate() {
        return toDate;
    }

    public int length() {
        return Days.daysBetween(fromDate.toDateTime().toLocalDate(), toDate.toDateTime().toLocalDate()).getDays();
    }

    @Override
    public String toString() {
        return "[" + fromDate + "," + toDate + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fromDate == null) ? 0 : fromDate.hashCode());
        result = prime * result + ((toDate == null) ? 0 : toDate.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DateRange other = (DateRange) obj;
        if (fromDate == null) {
            if (other.fromDate != null)
                return false;
        } else if (!fromDate.equals(other.fromDate))
            return false;
        if (toDate == null) {
            if (other.toDate != null)
                return false;
        } else if (!toDate.equals(other.toDate))
            return false;
        return true;
    }

    public boolean contains(ShortDate shortDate) {
        return shortDate.compareTo(fromDate) >= 0 && shortDate.compareTo(toDate) < 0; // 不包含最后一天
    }

    public boolean contains(DateRange dateRange) {
        return this.fromDate.compareTo(dateRange.fromDate) <= 0
                && this.toDate.compareTo(dateRange.toDate) >= 0;
    }


    @Override
    public Iterator<ShortDate> iterator() {
        return new Iterator<ShortDate>() {

            private ShortDate current = fromDate;

            @Override
            public boolean hasNext() {
                return current.compareTo(toDate) < 0;
            }

            @Override
            public ShortDate next() {
                Preconditions.checkArgument(hasNext(), "已经没有下一个了");
                ShortDate shortDate = current;
                current = current.addDays(1); //后移一位
                return shortDate;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public List<ShortDate> toDates() {
        List<ShortDate> result = Lists.newArrayList();
        for(ShortDate shortDate : this) {
            result.add(shortDate);
        }
        return result;
    }

    @Override
    public int compareTo(DateRange other) {
        Preconditions.checkNotNull(other);
        int f = this.getFromDate().compareTo(other.getFromDate());
        if (0 == f) {
            return this.getToDate().compareTo(other.getToDate());
        }
        return f;
    }
}
