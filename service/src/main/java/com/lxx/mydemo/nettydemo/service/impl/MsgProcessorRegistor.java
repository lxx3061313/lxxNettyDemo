package com.lxx.mydemo.nettydemo.service.impl;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * @author lixiaoxiong
 * @version 2018-02-24
 */
@Service
public class MsgProcessorRegistor {
    private Map<Integer, MsgProcessor> processors = new HashMap<>();

    public void reg(Integer msgCode, MsgProcessor processor) {
        processors.put(msgCode, processor);
    }

    public MsgProcessor select(Integer msgCode) {
        return processors.get(msgCode);
    }
}
