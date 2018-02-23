package com.lxx.mydemo.nettydemo.service.bean;

import com.lxx.mydemo.nettydemo.service.bean.P808Msg.P808MsgHeader;
import com.lxx.mydemo.nettydemo.service.common.util.BitOperator;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.commons.io.Charsets;

/**
 * 终端注册应答
 * @author lixiaoxiong
 * @version 2018-02-13
 */
public class P808TerminalRegResp {


    public static TerRegRespBuilder createRespBuilder() {
        return new TerRegRespBuilder();
    }

    public static class TerRegRespBuilder {
        private int msgId;
        private int msgBodyPropsField;
        private String terminalId;
        private int flowId;
        private byte[] msgBodyBytes;
        public TerRegRespBuilder setMsgId(int msgId) {
            this.msgId = msgId;
            return this;
        }

        public TerRegRespBuilder setBodyField(int bodyField) {
            this.msgBodyPropsField = bodyField;
            return this;
        }

        public TerRegRespBuilder setTerminalId(String terminalId) {
            this.terminalId = terminalId;
            return this;
        }

        public TerRegRespBuilder setFlowId(int flowId) {
            this.flowId = flowId;
            return this;
        }

        public TerRegRespBuilder setBody(byte [] body) {
            this.msgBodyBytes = body;
            return this;
        }

        public P808Msg build() {
            if (msgBodyBytes == null || msgBodyBytes.length == 0) {
                throw new IllegalArgumentException("msg body can not be empty");
            }

            P808MsgHeader header = new P808MsgHeader();
            header.setMsgId(msgId);
            header.setMsgBodyPropsField(msgBodyPropsField);
            header.setTerminalPhone(terminalId);
            header.setFlowId(flowId);

            P808Msg msg = new P808Msg();
            msg.setMsgHeader(header);
            msg.setMsgBodyBytes(msgBodyBytes);
            return msg;
        }
    }
}
