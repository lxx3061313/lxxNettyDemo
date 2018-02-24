package com.lxx.mydemo.nettydemo.service.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lixiaoxiong
 * @version 2018-02-24
 */
public final class TerminalParamItem {
    private TerminalParamType paramType;
    private int value;
    private String strValue;

    public TerminalParamType getParamType() {
        return paramType;
    }

    public void setParamType(TerminalParamType paramType) {
        this.paramType = paramType;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getStrValue() {
        return strValue;
    }

    public void setStrValue(String strValue) {
        this.strValue = strValue;
    }
}
