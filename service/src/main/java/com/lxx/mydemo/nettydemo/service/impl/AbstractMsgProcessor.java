package com.lxx.mydemo.nettydemo.service.impl;

import com.lxx.mydemo.nettydemo.service.bean.P808Msg;
import com.lxx.mydemo.nettydemo.service.bean.P808Msg.P808MsgHeader;
import io.netty.channel.ChannelHandlerContext;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author lixiaoxiong
 * @version 2018-02-24
 */
public abstract class AbstractMsgProcessor implements MsgProcessor {

    @Resource
    MsgProcessorRegistor msgProcessorRegistor;

    @Override
    public void process(P808Msg msg, ChannelHandlerContext ctx) {
        doProcess(msg.getMsgHeader(), msg.getMsgBodyBytes(), ctx);
        postProcess(msg.getMsgHeader(), msg.getMsgBodyBytes(), ctx);
    }

    abstract void doProcess(P808MsgHeader header, byte[] body, ChannelHandlerContext ctx);

    abstract int msgCode();

    abstract void postProcess(P808MsgHeader header, byte[] body, ChannelHandlerContext ctx);

    @PostConstruct
    private void init() {
        msgProcessorRegistor.reg(msgCode(), this);
    }
}
