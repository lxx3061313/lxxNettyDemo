package com.lxx.mydemo.nettydemo.service.bean;

/**
 * @author lixiaoxiong
 * @version 2018-02-13
 */
public final class P808MsgType {
    private int code;
    private String desc;

    public P808MsgType(int code, String desc) {
        this.code = code;
        this.desc = desc;
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

    public static final P808MsgType TERMINAL_REG = new P808MsgType(0x0100, "终端注册消息");
    public static final P808MsgType TERMINAL_COMMON_RESP = new P808MsgType(0x0001, "终端通用应答消息");
    public static final P808MsgType PLATFORM_COMMON_RESP = new P808MsgType(0x8001, "平台通用应答消息");
    public static final P808MsgType TERMIMAL_REQ_RESP = new P808MsgType(0x8100, "终端注册应答消息");
    public static final P808MsgType TERMIMAL_AUTH = new P808MsgType(0x0102, "终端鉴权消息");
    public static final P808MsgType TERMIMAL_HEART_BEAT = new P808MsgType(0x0002, "终端心跳消息");
    public static final P808MsgType TERMIMAL_REGULAR_REPORT = new P808MsgType(0x0200, "终端定时汇报消息");
    public static final P808MsgType QUERY_TERMINAL_PARAM = new P808MsgType(0x8104, "查询终端参数");
    public static final P808MsgType QUERY_TERMINAL_EXACT_PARAM = new P808MsgType(0x8106, "查询终端指定参数");
    public static final P808MsgType QUERY_TERMINAL_PARAM_RESP = new P808MsgType(0x0104, "查询终端参数应答");
    public static final P808MsgType QUERY_TERMINAL_ATTR = new P808MsgType(0x8107, "查询终端属性");
    public static final P808MsgType QUERY_TERMINAL_ATTR_RESP = new P808MsgType(0x0107, "查询终端属性应答");
    public static final P808MsgType DEVICE_JOIN_TERMINAL_NET = new P808MsgType(0x0F01, "智能锁入网报告");
    public static final P808MsgType DEVICE_LEAVE_TERMINAL_NET = new P808MsgType(0x0F02, "智能锁脱网报告");
}
