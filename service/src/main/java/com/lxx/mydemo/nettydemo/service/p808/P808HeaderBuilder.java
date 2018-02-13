package com.lxx.mydemo.nettydemo.service.p808;

import com.lxx.mydemo.nettydemo.service.common.util.BitOperator;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author lixiaoxiong
 * @version 2018-02-13
 */
public class P808HeaderBuilder {

    private static BitOperator bitOperator =  new BitOperator();

    /**
     * 消息id(2字节)
     */
    private int msgId;

    /**
     * 是否有子包
     */
    private boolean hasSubPackage;

    /**
     * 数据加密方式
     */
    private int encryptionType = 0;

    /**
     * 消息体长度(10 bit)
     */
    private int msgBodyLength;


    /**
     * 设备编码(6字节, 8421码)
     */
    private byte[] terminalId = new byte[6];


    /**
     * 消息流水号(2字节)
     */
    private int msgFlowId;

    /**
     * (如果分包的话)总包数
     */
    private int totalPackCount;


    /**
     * (如果分包的话)包序号
     */
    private int packNum;


    public P808HeaderBuilder setMsgId(int msgId) {
        this.msgId = msgId;
        return this;
    }

    public P808HeaderBuilder setSubPackFlag(boolean subPackFlag) {
        this.hasSubPackage = subPackFlag;
        return this;
    }

    public P808HeaderBuilder setEncryType(int type) {
        this.encryptionType = type;
        return this;
    }

    public P808HeaderBuilder setMsgBodyLength(int length) {
        this.msgBodyLength = length;
        return this;
    }

    public P808HeaderBuilder setTerminalId(byte [] id) {
        this.terminalId = id;
        return this;
    }

    public P808HeaderBuilder setMsgFlowId(int flowId) {
        this.msgFlowId = flowId;
        return this;
    }

    public P808HeaderBuilder setTotalPackCount(int total) {
        this.totalPackCount = total;
        return this;
    }

    public P808HeaderBuilder setSubPackNum(int num) {
        this.packNum = num;
        return this;
    }

    public byte[] build() {

        ByteBuf buf = Unpooled.buffer();
        //1. 消息id
        buf.writeBytes(bitOperator.integerTo2Bytes(this.msgId));

        //2. 消息体属性
        buf.writeBytes(buildMsgBodyField());

        //3. 终端手机号
        buf.writeBytes(this.terminalId);

        //4. 消息流水号
        buf.writeBytes(bitOperator.integerTo2Bytes(this.msgFlowId));


        if (this.hasSubPackage) {
            buf.writeBytes(bitOperator.integerTo2Bytes(this.totalPackCount));
            buf.writeBytes(bitOperator.integerTo2Bytes(this.packNum));
        }

        byte [] result = new byte[buf.readableBytes()];
        buf.readBytes(result);
        return result;
    }

    private int buildMsgBodyField() {
        //1. 消息体长度 0000 0011 1111 1111 [0-9]位表示
        int length = this.msgBodyLength & 0x03ff;

        //2. 数据加密方式 0001 1100 0000 0000
        int encry = (this.encryptionType  & 0x0008) << 10;

        //3. 分包标记 0010 0000 0000 0000
        int flag = this.hasSubPackage ? 1 << 13 : 0;

        //4. 保留 1100 0000 0000 0000
        int reservce = 0;

        return length | encry | flag | reservce;
    }
}
