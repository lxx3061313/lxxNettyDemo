package com.lxx.mydemo.nettydemo.service.bean;

import java.nio.channels.Channel;

/**
 * @author lixiaoxiong
 * @version 2018-02-12
 */
public class P808Msg {

    /**
     * 消息头
     */
    private P808MsgHeader msgHeader;

    /**
     * 消息体字节数组
     */
    private byte[] msgBodyBytes;

    /**
     * 校验码(1byte)
     */
    private int checkSum;

    /**
     * 记录每个客户端的channel,以便下发信息给客户端
     */
    private Channel channel;


    public P808MsgHeader getMsgHeader() {
        return msgHeader;
    }

    public void setMsgHeader(P808MsgHeader msgHeader) {
        this.msgHeader = msgHeader;
    }

    public byte[] getMsgBodyBytes() {
        return msgBodyBytes;
    }

    public void setMsgBodyBytes(byte[] msgBodyBytes) {
        this.msgBodyBytes = msgBodyBytes;
    }

    public int getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(int checkSum) {
        this.checkSum = checkSum;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public static class P808MsgHeader {
        // 消息ID
        protected int msgId;

        /////// ========消息体属性
        // byte[2-3]
        protected int msgBodyPropsField;
        // 消息体长度
        protected int msgBodyLength;
        // 数据加密方式
        protected int encryptionType;
        // 是否分包,true==>有消息包封装项
        protected boolean hasSubPackage;
        // 保留位[14-15]
        protected String reservedBit;
        /////// ========消息体属性

        // 终端手机号
        protected String terminalPhone;

        // 流水号
        protected int flowId;

        //////// =====消息包封装项
        // byte[12-15]
        protected int packageInfoField;
        // 消息包总数(word(16))
        protected long totalSubPackage;
        // 包序号(word(16))这次发送的这个消息包是分包中的第几个消息包, 从 1 开始
        protected long subPackageSeq;

        public int getMsgId() {
            return msgId;
        }

        public void setMsgId(int msgId) {
            this.msgId = msgId;
        }

        public int getMsgBodyPropsField() {
            return msgBodyPropsField;
        }

        public void setMsgBodyPropsField(int msgBodyPropsField) {
            this.msgBodyPropsField = msgBodyPropsField;
        }

        public int getMsgBodyLength() {
            return msgBodyLength;
        }

        public void setMsgBodyLength(int msgBodyLength) {
            this.msgBodyLength = msgBodyLength;
        }

        public int getEncryptionType() {
            return encryptionType;
        }

        public void setEncryptionType(int encryptionType) {
            this.encryptionType = encryptionType;
        }

        public boolean isHasSubPackage() {
            return hasSubPackage;
        }

        public void setHasSubPackage(boolean hasSubPackage) {
            this.hasSubPackage = hasSubPackage;
        }

        public String getReservedBit() {
            return reservedBit;
        }

        public void setReservedBit(String reservedBit) {
            this.reservedBit = reservedBit;
        }

        public String getTerminalPhone() {
            return terminalPhone;
        }

        public void setTerminalPhone(String terminalPhone) {
            this.terminalPhone = terminalPhone;
        }

        public int getFlowId() {
            return flowId;
        }

        public void setFlowId(int flowId) {
            this.flowId = flowId;
        }

        public int getPackageInfoField() {
            return packageInfoField;
        }

        public void setPackageInfoField(int packageInfoField) {
            this.packageInfoField = packageInfoField;
        }

        public long getTotalSubPackage() {
            return totalSubPackage;
        }

        public void setTotalSubPackage(long totalSubPackage) {
            this.totalSubPackage = totalSubPackage;
        }

        public long getSubPackageSeq() {
            return subPackageSeq;
        }

        public void setSubPackageSeq(long subPackageSeq) {
            this.subPackageSeq = subPackageSeq;
        }
    }
}
