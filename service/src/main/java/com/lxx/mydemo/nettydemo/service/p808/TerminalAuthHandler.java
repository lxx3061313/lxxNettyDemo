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
public class TerminalAuthHandler extends ChannelInboundHandlerAdapter {
    private final static Logger logger  = LoggerFactory.getLogger(TerminalAuthHandler.class);

    @Resource
    P808ConnFlowIdManager p808ConnFlowIdManager;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        P808Msg m = (P808Msg) msg;
        P808MsgHeader msgHeader = m.getMsgHeader();
        if (msgHeader.getMsgId() == P808MsgType.TERMIMAL_AUTH.getCode()) {
            logger.info("鉴权消息");

            PlatCommRespMsgBuilder builder = PlatCommRespMsgBuilder.createBuilder();

            int flowId = p808ConnFlowIdManager.getFlowId(ctx.channel().id().asLongText());
            //body
            builder.setRespFlowId(msgHeader.getFlowId());
            builder.setRespMsgId(P808MsgType.TERMIMAL_AUTH.getCode());
            builder.setResult((byte)0);

            //header
            builder.setTermimalId(msgHeader.getTerminalPhone())
                    .setFlowId(flowId);
            P808Msg build = builder.build();
            ctx.writeAndFlush(build);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
