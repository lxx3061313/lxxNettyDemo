package com.lxx.mydemo.nettydemo.service.impl;

import com.lxx.mydemo.nettydemo.service.bean.P808Msg.P808MsgHeader;
import com.lxx.mydemo.nettydemo.service.bean.P808MsgType;
import com.lxx.mydemo.nettydemo.service.bean.TerminalParamResp;
import com.lxx.mydemo.nettydemo.service.common.util.json.JsonUtil;
import com.lxx.mydemo.nettydemo.service.p808.TerminalParamRespDecoder;
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
public class TerminalParamRespProcessor extends AbstractMsgProcessor {
    private final static Logger logger  = LoggerFactory.getLogger(TerminalParamRespProcessor.class);


    @Resource
    TerminalParamRespDecoder terminalParamRespDecoder;

    @Override
    void doProcess(P808MsgHeader header, byte[] body, ChannelHandlerContext ctx) {
        logger.info("查询终端参数应答消息");
        TerminalParamResp decode = terminalParamRespDecoder.decode(body);
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
