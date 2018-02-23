package com.lxx.mydemo.nettydemo.service.p808;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

/**
 * @author lixiaoxiong
 * @version 2018-02-23
 */
@Service
public class P808ConnFlowIdManager {
    private Map<String, Integer> flowIdMap = new ConcurrentHashMap<>();

    //todo 以后换成redis
    synchronized public int getFlowId(String channelId) {
        Integer integer = flowIdMap.get(channelId);
        if (integer == null) {
            integer = 0;
            flowIdMap.put(channelId, 0);
        } else {
            flowIdMap.put(channelId, ++ integer);
        }
        return integer;
    }
}
