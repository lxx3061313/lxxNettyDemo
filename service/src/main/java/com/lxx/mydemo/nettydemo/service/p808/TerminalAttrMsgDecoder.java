package com.lxx.mydemo.nettydemo.service.p808;

import com.lxx.mydemo.nettydemo.service.bean.TerminalAttrResp;
import com.lxx.mydemo.nettydemo.service.common.util.BitOperator;
import java.nio.charset.Charset;
import org.springframework.stereotype.Service;

/**
 * @author lixiaoxiong
 * @version 2018-02-24
 */
@Service
public class TerminalAttrMsgDecoder {
    public TerminalAttrResp decode(byte[] body) {
        TerminalAttrResp attrResp = new TerminalAttrResp();
        attrResp.setTerminalType(BitOperator.parseIntFromBytes(body, 0, 2));
        attrResp.setMakerId(BitOperator.subBytes(body, 2, 5));
        attrResp.setTerminalModel(BitOperator.subBytes(body, 7, 20));
        attrResp.setTerminalId(BitOperator.subBytes(body, 27, 7));
        attrResp.setIccid(BitOperator.subBytes(body, 34, 10));

        int startIndex = 44;
        int hardwardVersionLenth = BitOperator.parseIntFromBytes(body, startIndex, 1);
        ++startIndex;

        String gbk = BitOperator.parseStringFromBytes(body, startIndex, hardwardVersionLenth,
                Charset.forName("GBK"));
        attrResp.setHardwardVersion(gbk);
        startIndex += hardwardVersionLenth;

        int firmwardVerLth = BitOperator.parseIntFromBytes(body, startIndex, 1);
        ++startIndex;

        String firmwardver = BitOperator.parseStringFromBytes(body, startIndex, firmwardVerLth, Charset.forName("GBK"));
        attrResp.setFirmwareVersion(firmwardver);
        return attrResp;
    }
}
