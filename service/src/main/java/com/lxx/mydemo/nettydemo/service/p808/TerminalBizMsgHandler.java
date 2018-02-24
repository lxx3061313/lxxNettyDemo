package com.lxx.mydemo.nettydemo.service.p808;

import com.lxx.mydemo.nettydemo.service.bean.P808Msg;
import com.lxx.mydemo.nettydemo.service.impl.MsgProcessor;
import com.lxx.mydemo.nettydemo.service.impl.MsgProcessorRegistor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.util.Optional;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lixiaoxiong
 * @version 2018-02-24
 */
public class TerminalBizMsgHandler extends ChannelInboundHandlerAdapter {
    private final static Logger logger  = LoggerFactory.getLogger(TerminalBizMsgHandler.class);

    @Resource
    P808SessionManager p808SessionManager;

    @Resource
    MsgProcessorRegistor msgProcessorRegistor;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        P808Msg m = (P808Msg)msg;
        // 保存channel
        p808SessionManager.keepChannel(m.getMsgHeader().getTerminalPhone(), (NioSocketChannel) ctx.channel());

        Optional<MsgProcessor> processor = Optional
                .ofNullable(msgProcessorRegistor.select(m.getMsgHeader().getMsgId()));
        if (processor.isPresent()) {
            processor.get().process(m, ctx);
        }

        ctx.fireChannelRead(msg);
    }
}
