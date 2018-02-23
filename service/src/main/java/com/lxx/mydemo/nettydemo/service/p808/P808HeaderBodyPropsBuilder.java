package com.lxx.mydemo.nettydemo.service.p808;

/**
 * @author lixiaoxiong
 * @version 2018-02-23
 */
public class P808HeaderBodyPropsBuilder {
    private boolean hasSubPack;
    private int encryType;
    private int msgBodyLength;

    private P808HeaderBodyPropsBuilder() {
    }

    public static PropsBuilder createBuilder() {
        return new PropsBuilder();
    }
    public static class PropsBuilder {
        private boolean hasSubPack;
        private int encryType;
        private int msgBodyLength;

        public PropsBuilder setSubPackFlag(boolean subFlag) {
            this.hasSubPack = subFlag;
            return this;
        }

        public PropsBuilder setencryType(int encryType) {
            this.encryType = encryType;
            return this;
        }

        public PropsBuilder setMsgBodyLength(int length) {
            this.msgBodyLength = length;
            return this;
        }

        public int buildPropsField() {
            //1. 消息体长度 0000 0011 1111 1111 [0-9]位表示
            int length = this.msgBodyLength & 0x03ff;

            //2. 数据加密方式 0001 1100 0000 0000
            int encry = (this.encryType  & 0x0008) << 10;

            //3. 分包标记 0010 0000 0000 0000
            int flag = this.hasSubPack ? 1 << 13 : 0;

            //4. 保留 1100 0000 0000 0000
            int reservce = 0;

            return length | encry | flag | reservce;
        }
    }
}
