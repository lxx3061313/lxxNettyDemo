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
    private String terminalId;
    private int flowId;

    public P808MsgBuilder setTermimalId(String terminalId) {
        this.terminalId = terminalId;
        return this;
    }

    public P808MsgBuilder setFlowId(int flowId) {
        this.flowId = flowId;
        return this;
    }

    abstract byte [] buildBody();

    abstract int getMsgId();

    public P808Msg build() {
        byte [] msgBodyBytes = buildBody();
        P808MsgHeader header = new P808MsgHeader();
        header.setMsgId(getMsgId());
        header.setMsgBodyPropsField(buildBodyField(msgBodyBytes.length));
        header.setTerminalPhone(terminalId);
        header.setFlowId(flowId);
        P808Msg msg = new P808Msg();
        msg.setMsgHeader(header);
        msg.setMsgBodyBytes(msgBodyBytes);
        return msg;
    }

    /**
     * 构建消息体属性字段
     * @param bodyLength 可能为0
     * @return 返回构建完成的字段
     */
    private int buildBodyField(int bodyLength) {
        PropsBuilder builder = P808HeaderBodyPropsBuilder.createBuilder();
        builder.setSubPackFlag(false);
        builder.setencryType(0);
        builder.setMsgBodyLength(bodyLength);
        return builder.buildPropsField();
    }
}
