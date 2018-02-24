package com.lxx.mydemo.nettydemo.service.p808;

import com.lxx.mydemo.nettydemo.service.bean.P808Msg;
import com.lxx.mydemo.nettydemo.service.bean.P808Msg.P808MsgHeader;
import com.lxx.mydemo.nettydemo.service.bean.P808MsgType;
import com.lxx.mydemo.nettydemo.service.p808.msgbuilder.P808TerReqMsgBuilder;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.nio.channels.SocketChannel;
import javax.annotation.Resource;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * 终端注册消息
 * @author lixiaoxiong
 * @version 2018-02-13
 */
@Sharable
public class TerminalRegHandler extends ChannelInboundHandlerAdapter {
    private final static Logger logger  = LoggerFactory.getLogger(TerminalRegHandler.class);

    @Resource
    P808ConnFlowIdManager p808ConnFlowIdManager;

    @Resource
    P808SessionManager p808SessionManager;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        P808Msg m = (P808Msg) msg;
        P808MsgHeader msgHeader = m.getMsgHeader();
        if (msgHeader == null) {
            logger.error("808协议缺少消息头部信息");
            return;
        }

        if (msgHeader.getMsgId() == P808MsgType.TERMINAL_REG.getCode()) {
            logger.info("收到终端注册消息");

            // 相应终端注册应答
            P808TerReqMsgBuilder respBuilder = P808TerReqMsgBuilder.createReqRespBuilder();
            // body
            respBuilder.setRespFlowId(msgHeader.getFlowId())
                    .setRespResult((byte)0)
                    .setAuthCode("123456");
            // header
            respBuilder.setTermimalId(msgHeader.getTerminalPhone())
                    .setFlowId(p808ConnFlowIdManager.getFlowId(ctx.channel().id().asLongText()));
            P808Msg build = respBuilder.build();

            // 保存channel
            p808SessionManager.keepChannel(msgHeader.getTerminalPhone(), (NioSocketChannel) ctx.channel());
            ctx.writeAndFlush(build);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("接收终端注册消息异常", cause);
    }
}
