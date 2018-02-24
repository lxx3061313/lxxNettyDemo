package com.lxx.mydemo.nettydemo.service.p808;

import com.lxx.mydemo.nettydemo.service.bean.TerminalParamItem;
import com.lxx.mydemo.nettydemo.service.bean.TerminalParamResp;
import com.lxx.mydemo.nettydemo.service.bean.TerminalParamType;
import com.lxx.mydemo.nettydemo.service.common.util.BitOperator;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * @author lixiaoxiong
 * @version 2018-02-24
 */
@Service
public class TerminalParamRespDecoder {

    public TerminalParamResp decode(byte[] body) {
        TerminalParamResp resp = new TerminalParamResp();
        resp.setRespFlowId(BitOperator.parseIntFromBytes(body, 0, 2));
        resp.setParamCount(BitOperator.parseIntFromBytes(body, 2, 1));
        resp.setParams(decode(resp.getParamCount(), 3, body));
        return resp;
    }

    private List<TerminalParamItem> decode(int count, int startIndex, byte[] body) {
        int start  = startIndex;

        List<TerminalParamItem> result = new ArrayList<>();
        for (int i=0; i< count; ++i) {
            TerminalParamItem item = new TerminalParamItem();
            int code = BitOperator.parseIntFromBytes(body, start, 4);
            start += 4;
            item.setParamType(TerminalParamType.codeOf(code));


            int valueLength = BitOperator.parseIntFromBytes(body, start, 1);
            start += 1;
            if (item.getParamType().getCode() != TerminalParamType.SERVER_HOST.getCode()) {
                item.setValue(BitOperator.parseIntFromBytes(body, start, valueLength));
            } else {
                item.setStrValue(BitOperator.parseStringFromBytes(body, start, valueLength, Charset.forName("GBK")));
            }
            start += valueLength;
            result.add(item);
        }
        return result;
    }
}
