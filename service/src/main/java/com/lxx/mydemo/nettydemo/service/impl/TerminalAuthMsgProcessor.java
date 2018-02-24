package com.lxx.mydemo.nettydemo.service.impl;

import com.lxx.mydemo.nettydemo.service.bean.P808Msg;
import com.lxx.mydemo.nettydemo.service.bean.P808Msg.P808MsgHeader;
import com.lxx.mydemo.nettydemo.service.bean.P808MsgType;
import com.lxx.mydemo.nettydemo.service.p808.P808ConnFlowIdManager;
import com.lxx.mydemo.nettydemo.service.p808.msgbuilder.PlatCommRespMsgBuilder;
import io.netty.channel.ChannelHandlerContext;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author lixiaoxiong
 * @version 2018-02-24
 */
@Service
public class TerminalAuthMsgProcessor extends AbstractMsgProcessor {
    private final static Logger logger  = LoggerFactory.getLogger(TerminalAuthMsgProcessor.class);

    @Resource
    P808ConnFlowIdManager p808ConnFlowIdManager;


    @Override
    void doProcess(P808MsgHeader header, byte[] body, ChannelHandlerContext ctx) {
        logger.info("鉴权消息");
    }

    @Override
    int msgCode() {
        return P808MsgType.TERMIMAL_AUTH.getCode();
    }

    @Override
    void postProcess(P808MsgHeader header, byte[] body, ChannelHandlerContext ctx) {
        int flowId = p808ConnFlowIdManager.getFlowId(ctx.channel().id().asLongText());
        PlatCommRespMsgBuilder builder = PlatCommRespMsgBuilder.createBuilder();
        //body
        builder.setRespFlowId(header.getFlowId());
        builder.setRespMsgId(P808MsgType.TERMIMAL_AUTH.getCode());
        builder.setResult((byte)0);

        //header
        builder.setTermimalId(header.getTerminalPhone())
                .setFlowId(flowId);
        P808Msg build = builder.build();
        ctx.writeAndFlush(build);
    }
}
