package com.lxx.mydemo.nettydemo.service.bean;

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
    private final static BitOperator bitOperator = new BitOperator();
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

    public int getRespFlowId() {
        return respFlowId;
    }

    public void setRespFlowId(int respFlowId) {
        this.respFlowId = respFlowId;
    }

    public byte getRespResult() {
        return respResult;
    }

    public void setRespResult(byte respResult) {
        this.respResult = respResult;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }


    static class Builder {
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

        public Builder setFlowId(int flowId) {
            this.respFlowId = flowId;
            return this;
        }

        public Builder setResult(byte result) {
            this.respResult = result;
            return this;
        }

        public Builder setAuthCode(String code) {
            this.authCode = code;
            return this;
        }

        public byte[] build() {
            ByteBuf buf = Unpooled.buffer();
            buf.writeBytes(bitOperator.integerTo2Bytes(this.respFlowId));
            buf.writeByte(this.respResult);
            buf.writeBytes(authCode.getBytes(Charsets.toCharset("GBK")));

            byte[] result = new byte[buf.readableBytes()];
            buf.readBytes(result);
            return result;
        }
    }
}
