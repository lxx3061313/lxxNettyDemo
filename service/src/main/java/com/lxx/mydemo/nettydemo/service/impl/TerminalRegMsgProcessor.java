package com.lxx.mydemo.nettydemo.service.impl;

import com.lxx.mydemo.nettydemo.service.bean.P808Msg;
import com.lxx.mydemo.nettydemo.service.bean.P808Msg.P808MsgHeader;
import com.lxx.mydemo.nettydemo.service.bean.P808MsgType;
import com.lxx.mydemo.nettydemo.service.p808.P808ConnFlowIdManager;
import com.lxx.mydemo.nettydemo.service.p808.TerminalRegHandler;
import com.lxx.mydemo.nettydemo.service.p808.msgbuilder.P808TerReqMsgBuilder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author lixiaoxiong
 * @version 2018-02-24
 */
@Service
public class TerminalRegMsgProcessor extends AbstractMsgProcessor {
    private final static Logger logger  = LoggerFactory.getLogger(TerminalRegMsgProcessor.class);

    @Resource
    P808ConnFlowIdManager p808ConnFlowIdManager;

    @Override
    void doProcess(P808MsgHeader header, byte[] body, ChannelHandlerContext ctx) {
        logger.info("收到终端注册消息");
    }

    @Override
    int msgCode() {
        return P808MsgType.TERMINAL_REG.getCode();
    }

    @Override
    void postProcess(P808MsgHeader header, byte[] body, ChannelHandlerContext ctx) {
        // 相应终端注册应答
        P808TerReqMsgBuilder respBuilder = P808TerReqMsgBuilder.createReqRespBuilder();
        // body
        respBuilder.setRespFlowId(header.getFlowId())
                .setRespResult((byte)0)
                .setAuthCode("123456");
        // header
        respBuilder.setTermimalId(header.getTerminalPhone())
                .setFlowId(p808ConnFlowIdManager.getFlowId(ctx.channel().id().asLongText()));
        P808Msg build = respBuilder.build();
        ctx.writeAndFlush(build);
    }
}
