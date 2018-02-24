package com.lxx.mydemo.nettydemo.service.p808.msgbuilder;

import com.lxx.mydemo.nettydemo.service.bean.P808MsgType;
import com.lxx.mydemo.nettydemo.service.common.util.BitOperator;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lixiaoxiong
 * @version 2018-02-24
 */
public class TerminalParamQueryParamBuilder extends P808MsgBuilder {
    private List<Integer> paramCodeList = new ArrayList<>();

    public static TerminalParamQueryParamBuilder createBuilder() {
        return new TerminalParamQueryParamBuilder();
    }

    private TerminalParamQueryParamBuilder() {
    }

    public TerminalParamQueryParamBuilder setCodeList(List<Integer> list) {
        paramCodeList.addAll(list);
        return this;
    }

    public TerminalParamQueryParamBuilder setCode(Integer code) {
        paramCodeList.add(code);
        return this;
    }

    @Override
    byte[] buildBody() {
        ByteBuf buf = Unpooled.buffer();
        int paramCount = paramCodeList.size();
        if (paramCount == 0) {
            return new byte[0];
        }

        buf.writeByte(paramCount);
        for (int i=0; i< paramCount; ++i) {
            buf.writeBytes(BitOperator.integerTo4Bytes(paramCodeList.get(i)));
        }

        byte[] resutl = new byte[buf.readableBytes()];
        buf.readBytes(resutl);
        return resutl;
    }

    @Override
    int getMsgId() {
        return P808MsgType.QUERY_TERMINAL_EXACT_PARAM.getCode();
    }
}
