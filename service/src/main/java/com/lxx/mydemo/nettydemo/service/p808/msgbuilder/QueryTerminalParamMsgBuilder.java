package com.lxx.mydemo.nettydemo.service.p808.msgbuilder;

import com.lxx.mydemo.nettydemo.service.bean.P808Msg;
import com.lxx.mydemo.nettydemo.service.bean.P808MsgType;

/**
 * @author lixiaoxiong
 * @version 2018-02-24
 */
public class QueryTerminalParamMsgBuilder  extends P808MsgBuilder{

    public static QueryTerminalParamMsgBuilder createBuilder() {
        return new QueryTerminalParamMsgBuilder();
    }

    private QueryTerminalParamMsgBuilder() {
    }

    @Override
    byte[] buildBody() {
        return new byte[0];
    }

    @Override
    int getMsgId() {
        return P808MsgType.QUERY_TERMINAL_PARAM.getCode();
    }
}
