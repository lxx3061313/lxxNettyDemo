package com.lxx.mydemo.nettydemo.service.impl;

import com.lxx.mydemo.nettydemo.service.bean.P808Msg;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author lixiaoxiong
 * @version 2018-02-24
 */
public interface MsgProcessor {
    void process(P808Msg msg, ChannelHandlerContext ctx);
}
