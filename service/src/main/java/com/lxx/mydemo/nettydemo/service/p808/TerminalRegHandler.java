package com.lxx.mydemo.nettydemo.service.p808;

import com.lxx.mydemo.nettydemo.service.bean.P808Msg;
import com.lxx.mydemo.nettydemo.service.bean.P808Msg.P808MsgHeader;
import com.lxx.mydemo.nettydemo.service.bean.P808MsgType;
import com.lxx.mydemo.nettydemo.service.bean.P808TerReqBodyResp;
import com.lxx.mydemo.nettydemo.service.bean.P808TerReqBodyResp.Builder;
import com.lxx.mydemo.nettydemo.service.bean.P808TerminalRegResp;
import com.lxx.mydemo.nettydemo.service.bean.P808TerminalRegResp.TerRegRespBuilder;
import com.lxx.mydemo.nettydemo.service.p808.P808HeaderBodyPropsBuilder.PropsBuilder;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
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

            //相应终端注册应答
            TerRegRespBuilder respBuilder = P808TerminalRegResp.createRespBuilder();
            respBuilder.setMsgId(P808MsgType.TERMIMAL_REQ_RESP.getCode());

            byte[] bytes = buildBody(msgHeader.getFlowId());
            respBuilder.setBody(bytes);
            respBuilder.setTerminalId(msgHeader.getTerminalPhone());
            respBuilder.setFlowId(msgHeader.getFlowId() + 1);
            respBuilder.setBodyField(buildBodyField(bytes.length));
            P808Msg build = respBuilder.build();
            ctx.writeAndFlush(build);

        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private byte[] buildBody(int flowId) {
        Builder builder = P808TerReqBodyResp.ReqBodyBuilder();
        builder.setFlowId(flowId);
        builder.setAuthCode("123456");
        builder.setResult((byte)0);
        return builder.build();
    }

    private int buildBodyField(int bodyLength) {
        PropsBuilder builder = P808HeaderBodyPropsBuilder.createBuilder();
        builder.setSubPackFlag(false);
        builder.setencryType(0);
        builder.setMsgBodyLength(bodyLength);
        return builder.buildPropsField();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
