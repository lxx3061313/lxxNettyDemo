package com.lxx.mydemo.nettydemo.service.p808.msgbuilder;

import com.lxx.mydemo.nettydemo.service.bean.P808MsgType;
import com.lxx.mydemo.nettydemo.service.common.util.BitOperator;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author lixiaoxiong
 * @version 2018-02-23
 */
public class PlatCommRespMsgBuilder extends P808MsgBuilder{


    private int respFlowId;
    private int respMsgId;
    private byte respResult;

    public static PlatCommRespMsgBuilder createBuilder() {
        return new PlatCommRespMsgBuilder();
    }

    public PlatCommRespMsgBuilder() {
    }

    public PlatCommRespMsgBuilder setRespFlowId(int flowId) {
        this.respFlowId = flowId;
        return this;
    }

    @Override
    byte[] buildBody() {
        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes(BitOperator.integerTo2Bytes(this.respFlowId));
        buf.writeBytes(BitOperator.integerTo2Bytes(this.respMsgId));
        buf.writeByte(respResult);

        byte[] result = new byte[buf.readableBytes()];
        buf.readBytes(result);
        return result;
    }

    @Override
    int getMsgId() {
        return P808MsgType.PLATFORM_COMMON_RESP.getCode();
    }

    public PlatCommRespMsgBuilder setRespMsgId(int msgId) {
        this.respMsgId = msgId;
        return this;
    }

    public PlatCommRespMsgBuilder setResult(byte result) {
        this.respResult = result;
        return this;
    }
}
