package com.lxx.mydemo.nettydemo.service.p808;

import com.lxx.mydemo.nettydemo.service.bean.P808Msg;
import com.lxx.mydemo.nettydemo.service.bean.P808Msg.P808MsgHeader;
import com.lxx.mydemo.nettydemo.service.common.util.BCD8421Operater;
import com.lxx.mydemo.nettydemo.service.common.util.BitOperator;
import javax.annotation.Resource;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

/**
 * @author lixiaoxiong
 * @version 2018-02-12
 */
@Service("p808Decoder")
public class P808Decoder {
    private final static Logger logger = LoggerFactory.getLogger(P808Decoder.class);
    @Resource
    P808HeaderDecoder p808HeaderDecoder;

    private BitOperator bitOperator;
    private BCD8421Operater bcd8421Operater;

    public P808Decoder() {
        this.bitOperator = new BitOperator();
        this.bcd8421Operater = new BCD8421Operater();
    }

    public P808Msg parseMsg(byte[] msgBytes) {
        P808Msg msg = new P808Msg();
        P808MsgHeader header = p808HeaderDecoder.parseHeader(msgBytes);
        msg.setMsgHeader(header);

        byte []  body = new byte[header.getMsgBodyLength()];
        int bodyStartIndex = header.isHasSubPackage() ? 16 : 12;
        System.arraycopy(msgBytes, bodyStartIndex, body, 0, header.getMsgBodyLength());
        msg.setMsgBodyBytes(body);

        int checkSumInPkg = msgBytes[msgBytes.length - 1];
        int checkSum = bitOperator.getCheckSum4JT808(msgBytes, 0, msgBytes.length - 1);
        if (checkSum != checkSumInPkg) {
            logger.error("校验码验证失败");
        }
        msg.setCheckSum(checkSumInPkg);
        return msg;
    }
}
