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
}
