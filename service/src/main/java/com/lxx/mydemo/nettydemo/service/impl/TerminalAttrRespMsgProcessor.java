package com.lxx.mydemo.nettydemo.service.impl;

import com.lxx.mydemo.nettydemo.service.bean.P808Msg.P808MsgHeader;
import com.lxx.mydemo.nettydemo.service.bean.P808MsgType;
import com.lxx.mydemo.nettydemo.service.bean.TerminalAttrResp;
import com.lxx.mydemo.nettydemo.service.common.util.json.JsonUtil;
import com.lxx.mydemo.nettydemo.service.p808.TerminalAttrMsgDecoder;
import io.netty.channel.ChannelHandlerContext;
import javax.annotation.Resource;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

/**
 * @author lixiaoxiong
 * @version 2018-02-24
 */
@Service
public class TerminalAttrRespMsgProcessor extends AbstractMsgProcessor {
    private final static Logger logger = LoggerFactory.getLogger(TerminalAttrRespMsgProcessor.class);

    @Resource
    TerminalAttrMsgDecoder terminalAttrMsgDecoder;

    @Override
    void doProcess(P808MsgHeader header, byte[] body, ChannelHandlerContext ctx) {
        logger.info("终端属性消息");
        TerminalAttrResp decode = terminalAttrMsgDecoder.decode(body);
        logger.info("终端属性消息详情:{}", JsonUtil.of(decode));
    }

    @Override
    int msgCode() {
        return P808MsgType.QUERY_TERMINAL_ATTR_RESP.getCode();
    }

    @Override
    void postProcess(P808MsgHeader header, byte[] body, ChannelHandlerContext ctx) {

    }
}
