package com.lxx.mydemo.nettydemo.service.p808.msgbuilder;

import com.lxx.mydemo.nettydemo.service.bean.P808Msg;
import com.lxx.mydemo.nettydemo.service.bean.P808Msg.P808MsgHeader;
import com.lxx.mydemo.nettydemo.service.p808.P808HeaderBodyPropsBuilder;
import com.lxx.mydemo.nettydemo.service.p808.P808HeaderBodyPropsBuilder.PropsBuilder;

/**
 * @author lixiaoxiong
 * @version 2018-02-23
 */
public abstract class P808MsgBuilder {
    private int msgId;
    private String terminalId;
    private int flowId;

    public P808MsgBuilder setMsgId(int msgId) {
        this.msgId = msgId;
        return this;
    }

    public P808MsgBuilder setTermimalId(String terminalId) {
        this.terminalId = terminalId;
        return this;
    }

    public P808MsgBuilder setFlowId(int flowId) {
        this.flowId = flowId;
        return this;
    }

    abstract byte [] buildBody();

    public P808Msg build() {
        byte [] msgBodyBytes = buildBody();
        if (msgBodyBytes == null || msgBodyBytes.length == 0) {
            throw new IllegalArgumentException("msg body can not be empty");
        }

        P808MsgHeader header = new P808MsgHeader();
        header.setMsgId(msgId);
        header.setMsgBodyPropsField(buildBodyField(msgBodyBytes.length));
        header.setTerminalPhone(terminalId);
        header.setFlowId(flowId);
        P808Msg msg = new P808Msg();
        msg.setMsgHeader(header);
        msg.setMsgBodyBytes(msgBodyBytes);
        return msg;
    }

    private int buildBodyField(int bodyLength) {
        PropsBuilder builder = P808HeaderBodyPropsBuilder.createBuilder();
        builder.setSubPackFlag(false);
        builder.setencryType(0);
        builder.setMsgBodyLength(bodyLength);
        return builder.buildPropsField();
    }
}
