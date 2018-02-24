package com.lxx.mydemo.nettydemo.service.p808.msgbuilder;

import com.lxx.mydemo.nettydemo.service.bean.P808MsgType;
import com.lxx.mydemo.nettydemo.service.common.util.BitOperator;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import javax.annotation.Resource;
import org.apache.commons.io.Charsets;
import org.springframework.stereotype.Service;

/**
 * @author lixiaoxiong
 * @version 2018-02-23
 */
public class P808TerReqMsgBuilder extends P808MsgBuilder{

    /**
     * 应答流水号(2字节)
     */
    private int respFlowId;

    /**
     * 结果:0 成功,1车辆已注册 2数据库无该车辆 3 终端已被注册 4 数据库无该终端
     */
    private byte respResult;

    /**
     * 鉴权码
     */
    private String authCode;

    public static P808TerReqMsgBuilder createReqRespBuilder() {
        return new P808TerReqMsgBuilder();
    }

    private P808TerReqMsgBuilder() {
    }

    public P808TerReqMsgBuilder setRespFlowId(int flowId) {
        this.respFlowId = flowId;
        return this;
    }

    public P808TerReqMsgBuilder setRespResult(byte result) {
        this.respResult = result;
        return this;
    }

    public P808TerReqMsgBuilder setAuthCode(String authCode) {
        this.authCode = authCode;
        return this;
    }

    @Override
    byte[] buildBody() {
        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes(BitOperator.integerTo2Bytes(this.respFlowId));
        buf.writeByte(this.respResult);
        buf.writeBytes(authCode.getBytes(Charsets.toCharset("GBK")));
        byte[] result = new byte[buf.readableBytes()];
        buf.readBytes(result);
        return result;
    }

    @Override
    int getMsgId() {
        return P808MsgType.TERMIMAL_REQ_RESP.getCode();
    }
}
