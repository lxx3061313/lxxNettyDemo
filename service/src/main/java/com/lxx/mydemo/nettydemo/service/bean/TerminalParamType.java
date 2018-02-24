package com.lxx.mydemo.nettydemo.service.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lixiaoxiong
 * @version 2018-02-24
 */
public final class TerminalParamType {
    private int code;
    private String desc;
    private static List<TerminalParamType> items = new ArrayList<>();

    TerminalParamType(int code, String desc) {
        this.code = code;
        this.desc = desc;
        items.add(this);
    }

    public static TerminalParamType HEART_INTERVAL = new TerminalParamType(0x0001, "终端心跳发送间隔");
    public static TerminalParamType SERVER_HOST = new TerminalParamType(0x0013, "主服务器地址");
    public static TerminalParamType SERVER_PORT = new TerminalParamType(0x0018, "主服务器端口");
    public static TerminalParamType REPORT_INTERVAL = new TerminalParamType(0x0029, "终端汇报间隔");
    public static TerminalParamType TERMINAL_LATITUDE = new TerminalParamType(0x80E0, "终端所在纬度");
    public static TerminalParamType TERMINAL_LONGITUDE = new TerminalParamType(0x80E1, "终端所在经度");
    public static TerminalParamType TERMINAL_HEIGHT = new TerminalParamType(0x80E2, "终端所在海拔高度");

    public static TerminalParamType codeOf(int code) {
        for (TerminalParamType item : items) {
            if (item.getCode() == code) {
                return item;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
