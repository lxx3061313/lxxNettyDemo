package com.lxx.mydemo.nettydemo.service.p808;

import com.lxx.mydemo.nettydemo.service.bean.MsgConstants;
import com.lxx.mydemo.nettydemo.service.bean.P808Msg;
import com.lxx.mydemo.nettydemo.service.bean.P808Msg.P808MsgHeader;
import com.lxx.mydemo.nettydemo.service.common.util.BCD8421Operater;
import com.lxx.mydemo.nettydemo.service.common.util.BitOperator;
import com.lxx.mydemo.nettydemo.service.common.util.TransUtil;
import com.mysql.jdbc.log.Log;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * @author lixiaoxiong
 * @version 2018-02-13
 */
@Sharable
public class P808Encoder extends MessageToMessageEncoder<P808Msg>{
    private final static Logger logger = LoggerFactory.getLogger(P808Encoder.class);

    @Resource
    BitOperator bitOperator;

    @Resource
    BCD8421Operater bcd8421Operater;
    @Override
    protected void encode(ChannelHandlerContext ctx, P808Msg msg, List<Object> out)
            throws Exception {
        ByteBuf buf = Unpooled.buffer();
        buf.writeByte(MsgConstants.MSG_IDENTIFICATION_BIT);

        // encode header
        P808MsgHeader header = msg.getMsgHeader();

        //1. 消息id
        buf.writeBytes(BitOperator.integerTo2Bytes(header.getMsgId()));

        //2. 消息体属性
        buf.writeBytes(BitOperator.integerTo2Bytes(header.getMsgBodyPropsField()));

        //3. 设备id
        buf.writeBytes(bcd8421Operater.parseBytesFromString(header.getTerminalPhone()));

        //4. 消息流水号
        buf.writeBytes(BitOperator.integerTo2Bytes(header.getFlowId()));

        //5. 消息封装项
        if (header.isHasSubPackage()) {
            buf.writeBytes(BitOperator.integerTo2Bytes((int)header.getTotalSubPackage()));
            buf.writeBytes(BitOperator.integerTo2Bytes((int)header.getSubPackageSeq()));
        }


        //6. 消息体
        buf.writeBytes(msg.getMsgBodyBytes());

        //7. 校验码
        byte [] forCheckSum = new byte[buf.readableBytes()];
        buf.readBytes(forCheckSum);
        buf.writeByte(BitOperator.getCheckSum4JT808(forCheckSum, 1, forCheckSum.length - 1));

        //8. 标识位
        buf.writeByte(MsgConstants.MSG_IDENTIFICATION_BIT);
        buf.resetReaderIndex();

        byte [] forlog = new byte[buf.readableBytes()];
        buf.readBytes(forlog);
        buf.resetReaderIndex();
        logger.info("send bytes:{}", TransUtil.bytes2HexString(forlog));
        //out.add(buf);
    }
}
