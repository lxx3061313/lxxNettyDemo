package com.lxx.mydemo.nettydemo.service.p808;

import com.lxx.mydemo.nettydemo.service.bean.P808Msg;
import com.lxx.mydemo.nettydemo.service.bean.P808Msg.P808MsgHeader;
import com.lxx.mydemo.nettydemo.service.common.util.BCD8421Operater;
import com.lxx.mydemo.nettydemo.service.common.util.BitOperator;
import com.lxx.mydemo.nettydemo.service.common.util.ByteUtil;
import com.lxx.mydemo.nettydemo.service.common.util.json.JsonUtil;
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
 * @version 2018-02-12
 */
@Sharable
public class P808Decoder extends ChannelInboundHandlerAdapter {
    private final static Logger logger = LoggerFactory.getLogger(P808Decoder.class);
    @Resource
    P808HeaderDecoder p808HeaderDecoder;

    private BitOperator bitOperator;

    public P808Decoder() {
        this.bitOperator = new BitOperator();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        if (buf.readableBytes() == 0) {
            return;
        }

        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);

        // todo 加debugflag
        logger.info("receive bytes:{}", ByteUtil.bytes2HexString(bytes));

        P808Msg p808Msg = parseMsg(bytes);
        // todo 加debugflag
        logger.info("parse msg:{}", JsonUtil.of(p808Msg));
        ctx.fireChannelRead(p808Msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    public P808Msg parseMsg(byte[] msgBytes) {
        P808Msg msg = new P808Msg();
        P808MsgHeader header = p808HeaderDecoder.parseHeader(msgBytes);
        msg.setMsgHeader(header);

        byte []  body = new byte[header.getMsgBodyLength()];
        int bodyStartIndex = header.isHasSubPackage() ? 16 : 12;
        System.arraycopy(msgBytes, bodyStartIndex, body, 0, header.getMsgBodyLength());
        msg.setMsgBodyBytes(body);

        int checkSumInPkg = msgBytes[msgBytes.length - 1];
        int checkSum = bitOperator.getCheckSum4JT808(msgBytes, 0, msgBytes.length - 1);
        if (checkSum != checkSumInPkg) {
            logger.error("校验码验证失败");
        }
        msg.setCheckSum(checkSumInPkg);
        return msg;
    }
}
