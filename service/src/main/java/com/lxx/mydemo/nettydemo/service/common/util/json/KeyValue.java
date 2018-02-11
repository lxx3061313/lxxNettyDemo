package com.lxx.mydemo.nettydemo.service.common.util.json;

/**
 * @author zhenwei.liu
 * @since 2016-07-06
 */
public class KeyValue {

    private Object key;
    private Object value;

    public KeyValue() {
    }

    public KeyValue(Object key, Object value) {
        this.key = key;
        this.value = value;
    }

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
