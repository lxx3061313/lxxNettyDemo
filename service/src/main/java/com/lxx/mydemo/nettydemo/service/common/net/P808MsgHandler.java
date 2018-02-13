package com.lxx.mydemo.nettydemo.service.common.net;

import com.lxx.mydemo.nettydemo.service.bean.P808Msg;
import com.lxx.mydemo.nettydemo.service.common.util.ByteUtil;
import com.lxx.mydemo.nettydemo.service.common.util.json.JsonUtil;
import com.lxx.mydemo.nettydemo.service.p808.P808Decoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import javax.annotation.Resource;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

/**
 * @author lixiaoxiong
 * @version 2018-02-13
 */
@Sharable
public class P808MsgHandler extends ChannelInboundHandlerAdapter {
    private final static Logger logger = LoggerFactory.getLogger(P808MsgHandler.class);

    @Resource
    P808Decoder p808Decoder;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        if (buf.readableBytes() == 0) {
            return;
        }

        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        logger.info("receive bytes:{}", ByteUtil.bytes2HexString(bytes));

        P808Msg p808Msg = p808Decoder.parseMsg(bytes);
        logger.info("parse msg:{}", JsonUtil.of(p808Msg));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
