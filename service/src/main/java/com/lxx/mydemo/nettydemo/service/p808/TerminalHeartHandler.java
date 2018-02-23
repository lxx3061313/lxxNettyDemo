package com.lxx.mydemo.nettydemo.service.p808;

import com.lxx.mydemo.nettydemo.service.bean.P808Msg;
import com.lxx.mydemo.nettydemo.service.bean.P808Msg.P808MsgHeader;
import com.lxx.mydemo.nettydemo.service.bean.P808MsgType;
import com.lxx.mydemo.nettydemo.service.p808.msgbuilder.PlatCommRespMsgBuilder;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lixiaoxiong
 * @version 2018-02-23
 */
@Sharable
public class TerminalHeartHandler extends ChannelInboundHandlerAdapter {
    private final static Logger logger  = LoggerFactory.getLogger(TerminalHeartHandler.class);

    @Resource
    P808ConnFlowIdManager p808ConnFlowIdManager;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        P808Msg m = (P808Msg) msg;
        P808MsgHeader msgHeader = m.getMsgHeader();
        if (msgHeader.getMsgId() == P808MsgType.TERMIMAL_HEART_BEAT.getCode()) {
            logger.info("收到心跳消息");

            PlatCommRespMsgBuilder builder = PlatCommRespMsgBuilder.createBuilder();
            builder.setRespFlowId(p808ConnFlowIdManager.getFlowId(ctx.channel().id().asLongText()));
            builder.setRespMsgId(P808MsgType.TERMIMAL_HEART_BEAT.getCode());
            builder.setResult((byte)0);
        } else{
            ctx.fireChannelRead(msg);
        }
    }
}
