package com.lxx.mydemo.nettydemo.service.common.net;

import java.nio.ByteOrder;

/**
 * T808协议解码器解码消息长度计算器
 * @author lixiaoxiong
 * @version 2018-02-11
 */
public class MsgLengthCalculator {
    private ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
    private int maxFrameLength = 10000;

    /**
     * 标识位(1字节) + 消息头[消息ID(2字节) + 消息体属性(1字节---1字节消息体长度)]
     */
    private int lengthFieldOffset = 4;
}
