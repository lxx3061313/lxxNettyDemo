package com.lxx.mydemo.nettydemo.service.impl;

import com.lxx.mydemo.nettydemo.service.bean.P808Msg.P808MsgHeader;
import com.lxx.mydemo.nettydemo.service.bean.P808MsgType;
import com.lxx.mydemo.nettydemo.service.bean.TerminalParamResp;
import com.lxx.mydemo.nettydemo.service.common.util.json.JsonUtil;
import com.lxx.mydemo.nettydemo.service.p808.TerminalParamRespHandler;
import com.lxx.mydemo.nettydemo.service.p808.msgbuilder.TerminalParamRespBuilder;
import io.netty.channel.ChannelHandlerContext;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lixiaoxiong
 * @version 2018-02-24
 */
public class TerminalParamRespProcessor extends AbstractMsgProcessor {
    private final static Logger logger  = LoggerFactory.getLogger(TerminalParamRespProcessor.class);


    @Resource
    TerminalParamRespBuilder terminalParamRespBuilder;

    @Override
    void doProcess(P808MsgHeader header, byte[] body, ChannelHandlerContext ctx) {
        logger.info("查询终端参数应答消息");
        TerminalParamResp decode = terminalParamRespBuilder.decode(body);
        logger.info("终端参数应答消息:{}", JsonUtil.of(decode));
    }

    @Override
    int msgCode() {
        return P808MsgType.QUERY_TERMINAL_PARAM_RESP.getCode();
    }

    @Override
    void postProcess(P808MsgHeader header, byte[] body, ChannelHandlerContext ctx) {

    }
}
