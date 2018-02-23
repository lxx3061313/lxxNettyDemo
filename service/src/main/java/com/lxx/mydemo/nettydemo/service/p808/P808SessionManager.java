package com.lxx.mydemo.nettydemo.service.p808;

import io.netty.channel.socket.nio.NioSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

/**
 * @author lixiaoxiong
 * @version 2018-02-23
 */
@Service
public class P808SessionManager {
    private Map<String, NioSocketChannel> channelMap = new ConcurrentHashMap<>();

    public NioSocketChannel getChannel(String terminalId) {
        return channelMap.get(terminalId);
    }

    public void keepChannel(String terminalId, NioSocketChannel channel) {
        channelMap.put(terminalId, channel);
    }
}
