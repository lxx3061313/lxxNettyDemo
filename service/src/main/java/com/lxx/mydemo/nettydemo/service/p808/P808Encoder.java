package com.lxx.mydemo.nettydemo.service.p808;

import com.lxx.mydemo.nettydemo.service.bean.MsgConstants;
import com.lxx.mydemo.nettydemo.service.bean.P808Msg;
import com.lxx.mydemo.nettydemo.service.bean.P808Msg.P808MsgHeader;
import com.lxx.mydemo.nettydemo.service.common.util.BitOperator;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import java.util.List;
import javax.annotation.Resource;

/**
 * @author lixiaoxiong
 * @version 2018-02-13
 */
public class P808Encoder extends MessageToMessageEncoder<P808Msg>{

    @Resource
    BitOperator bitOperator;
    @Override
    protected void encode(ChannelHandlerContext ctx, P808Msg msg, List<Object> out)
            throws Exception {
        ByteBuf buf = Unpooled.buffer();
        buf.writeByte(MsgConstants.MSG_IDENTIFICATION_BIT);

        // encode header
        P808MsgHeader header = msg.getMsgHeader();

        //1. 消息id
        buf.writeBytes(bitOperator.integerTo2Bytes(header.getMsgId()));

        //2. 消息体属性
        buf.writeBytes(bitOperator.integerTo2Bytes(header.getMsgBodyPropsField()));

    }
}
