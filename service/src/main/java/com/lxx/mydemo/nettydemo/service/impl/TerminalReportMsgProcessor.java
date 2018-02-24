package com.lxx.mydemo.nettydemo.service.impl;

import com.lxx.mydemo.nettydemo.service.bean.BaseStationReportMsg;
import com.lxx.mydemo.nettydemo.service.bean.P808Msg;
import com.lxx.mydemo.nettydemo.service.bean.P808Msg.P808MsgHeader;
import com.lxx.mydemo.nettydemo.service.bean.P808MsgType;
import com.lxx.mydemo.nettydemo.service.common.util.json.JsonUtil;
import com.lxx.mydemo.nettydemo.service.p808.P808ConnFlowIdManager;
import com.lxx.mydemo.nettydemo.service.p808.TerminalAuthHandler;
import com.lxx.mydemo.nettydemo.service.p808.TerminalReportMsgDecoder;
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
public class TerminalReportMsgProcessor extends AbstractMsgProcessor {
    private final static Logger logger  = LoggerFactory.getLogger(TerminalReportMsgProcessor.class);

    @Resource
    TerminalReportMsgDecoder terminalReportMsgDecoder;

    @Resource
    P808ConnFlowIdManager p808ConnFlowIdManager;

    @Override
    void doProcess(P808MsgHeader header, byte[] body, ChannelHandlerContext ctx) {
        BaseStationReportMsg reportMsg = terminalReportMsgDecoder.decode(body);
        logger.info("终端定时汇报消息:{}", JsonUtil.of(reportMsg));
    }

    @Override
    int msgCode() {
        return P808MsgType.TERMIMAL_REGULAR_REPORT.getCode();
    }

    @Override
    void postProcess(P808MsgHeader header, byte[] body, ChannelHandlerContext ctx) {
        //回复平台通用消息
        PlatCommRespMsgBuilder builder = PlatCommRespMsgBuilder.createBuilder();

        //body
        builder.setRespFlowId(header.getFlowId());
        builder.setRespMsgId(msgCode());
        builder.setResult((byte)0);

        //header
        builder.setTermimalId(header.getTerminalPhone())
                .setFlowId(p808ConnFlowIdManager.getFlowId(ctx.channel().id().asLongText()));
        ctx.writeAndFlush(builder.build());
    }
}
