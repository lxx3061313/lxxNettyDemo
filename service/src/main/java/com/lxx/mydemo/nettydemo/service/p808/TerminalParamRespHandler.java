package com.lxx.mydemo.nettydemo.service.p808;

import com.lxx.mydemo.nettydemo.service.bean.P808Msg;
import com.lxx.mydemo.nettydemo.service.bean.P808Msg.P808MsgHeader;
import com.lxx.mydemo.nettydemo.service.bean.P808MsgType;
import com.lxx.mydemo.nettydemo.service.bean.TerminalParamResp;
import com.lxx.mydemo.nettydemo.service.common.util.json.JsonUtil;
import com.lxx.mydemo.nettydemo.service.p808.msgbuilder.TerminalParamRespBuilder;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lixiaoxiong
 * @version 2018-02-24
 */
@Sharable
public class TerminalParamRespHandler extends ChannelInboundHandlerAdapter {

    @Resource
    TerminalParamRespBuilder terminalParamRespBuilder;

    private final static Logger logger  = LoggerFactory.getLogger(TerminalParamRespHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        P808Msg m = (P808Msg) msg;
        P808MsgHeader msgHeader = m.getMsgHeader();
        if (P808MsgType.QUERY_TERMINAL_PARAM_RESP.getCode() == msgHeader.getMsgId()) {
            logger.info("查询终端参数应答消息");
            TerminalParamResp decode = terminalParamRespBuilder.decode(m.getMsgBodyBytes());
            logger.info("终端参数应答消息:{}",JsonUtil.of(decode));
        } else {
            ctx.fireChannelRead(msg);
        }
    }
}
