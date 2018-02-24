package com.lxx.mydemo.nettydemo.service.p808;

import com.lxx.mydemo.nettydemo.service.bean.BaseStationReportMsg;
import com.lxx.mydemo.nettydemo.service.bean.P808Msg;
import com.lxx.mydemo.nettydemo.service.bean.P808Msg.P808MsgHeader;
import com.lxx.mydemo.nettydemo.service.bean.P808MsgType;
import com.lxx.mydemo.nettydemo.service.common.util.json.JsonUtil;
import com.lxx.mydemo.nettydemo.service.p808.msgbuilder.PlatCommRespMsgBuilder;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.nio.NioSocketChannel;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lixiaoxiong
 * @version 2018-02-24
 */
@Sharable
public class RegularReportHandler extends ChannelInboundHandlerAdapter {

    private final static Logger logger  = LoggerFactory.getLogger(TerminalAuthHandler.class);

    @Resource
    TerminalReportMsgDecoder terminalReportMsgDecoder;

    @Resource
    P808ConnFlowIdManager p808ConnFlowIdManager;

    @Resource
    P808SessionManager p808SessionManager;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        P808Msg m = (P808Msg) msg;
        P808MsgHeader msgHeader = m.getMsgHeader();

        // 保存channel
        p808SessionManager.keepChannel(msgHeader.getTerminalPhone(), (NioSocketChannel) ctx.channel());
        if (P808MsgType.TERMIMAL_REGULAR_REPORT.getCode() == msgHeader.getMsgId()) {


            byte[] body = m.getMsgBodyBytes();
            BaseStationReportMsg reportMsg = terminalReportMsgDecoder.decode(body);
            logger.info("终端定时汇报消息:{}", JsonUtil.of(reportMsg));


            //回复平台通用消息
            PlatCommRespMsgBuilder builder = PlatCommRespMsgBuilder.createBuilder();

            //body
            builder.setRespFlowId(msgHeader.getFlowId());
            builder.setRespMsgId(P808MsgType.TERMIMAL_REGULAR_REPORT.getCode());
            builder.setResult((byte)0);

            //header
            builder.setTermimalId(msgHeader.getTerminalPhone())
                    .setFlowId(p808ConnFlowIdManager.getFlowId(ctx.channel().id().asLongText()));
            ctx.writeAndFlush(builder.build());
        } else {
            ctx.fireChannelRead(msg);
        }
    }
}
