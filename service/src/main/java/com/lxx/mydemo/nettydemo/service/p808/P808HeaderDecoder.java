package com.lxx.mydemo.nettydemo.service.p808;

import com.lxx.mydemo.nettydemo.service.bean.P808Msg.P808MsgHeader;
import com.lxx.mydemo.nettydemo.service.common.util.BCD8421Operater;
import com.lxx.mydemo.nettydemo.service.common.util.BitOperator;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author lixiaoxiong
 * @version 2018-02-12
 */
@Service("p808HeaderDecoder")
public class P808HeaderDecoder {

    @Resource
    BitOperator bitOperator;

    @Resource
    BCD8421Operater bcd8421Operater;

    public P808MsgHeader parseHeader(byte[] headerBytes) {
        P808MsgHeader header = new P808MsgHeader();

        //1. 消息id
        header.setMsgId(bitOperator.parseIntFromBytes(headerBytes, 0, 2));

        //2. 消息体属性
        int msgBodyProps = bitOperator.parseIntFromBytes(headerBytes, 2, 2);
        //2.1 消息体属性字段
        header.setMsgBodyPropsField(msgBodyProps);

        //2.2 消息体长度[0-9位]
        header.setMsgBodyLength(msgBodyProps & 0x03ff);

        //2.3 加密类型
        header.setEncryptionType((msgBodyProps & 0x1c00) >> 10);

        //2.4 是否有子包
        header.setHasSubPackage(((msgBodyProps & 0x2000) >> 13) == 1);

        //2.5 保留字段
        header.setReservedBit(((msgBodyProps & 0xc000) >> 14) + "");

        //3. 终端手机号
        header.setTerminalPhone(bcd8421Operater.parseBcdStringFromBytes(headerBytes, 4, 6, null));

        //4. 消息流水号
        header.setFlowId(bitOperator.parseIntFromBytes(headerBytes, 10 ,2));

        if (header.isHasSubPackage()) {
            header.setPackageInfoField(bitOperator.parseIntFromBytes(headerBytes, 12, 4));
            header.setTotalSubPackage(bitOperator.parseIntFromBytes(headerBytes, 12, 2));
            header.setSubPackageSeq(bitOperator.parseIntFromBytes(headerBytes, 14, 2));
        }
        return header;
    }
}
