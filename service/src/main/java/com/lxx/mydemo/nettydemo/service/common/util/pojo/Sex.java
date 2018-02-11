package com.lxx.mydemo.nettydemo.service.common.util.pojo;

import java.text.MessageFormat;

/**
 * 性别
 *
 * @author yushen.ma
 * @version 2016-06-27
 */
public enum Sex {

    MALE("男性",1),

    FEMALE("女性",2),

    UNKNOWN("未知",9);

    private final String desc;

    private int code;

    Sex(String desc, int code) {
        this.desc = desc;
        this.code = code;
    }

    public static Sex codeOf(int code){
        for (Sex s : values()){
            if (s.getCode() == code){
                return s;
            }
        }
        throw new IllegalArgumentException(MessageFormat.format("invalid sex code:{0}", code));
    }
    Sex(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
