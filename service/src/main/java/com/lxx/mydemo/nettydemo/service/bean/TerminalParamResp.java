package com.lxx.mydemo.nettydemo.service.bean;

import java.util.List;

/**
 * @author lixiaoxiong
 * @version 2018-02-24
 */
public class TerminalParamResp {
    private int respFlowId;
    private int paramCount;
    private List<TerminalParamItem> params;

    public int getRespFlowId() {
        return respFlowId;
    }

    public void setRespFlowId(int respFlowId) {
        this.respFlowId = respFlowId;
    }

    public int getParamCount() {
        return paramCount;
    }

    public void setParamCount(int paramCount) {
        this.paramCount = paramCount;
    }

    public List<TerminalParamItem> getParams() {
        return params;
    }

    public void setParams(List<TerminalParamItem> params) {
        this.params = params;
    }
}
