package com.lxx.mydemo.nettydemo.service.common.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author lixiaoxiong
 * @version 2018-02-11
 */
@Service
public class TcpServer {

    @Resource
    private ServerBootstrap serverBootstrap;

    private ChannelFuture future;

    @PostConstruct
    public void start() throws InterruptedException {
        future = serverBootstrap.bind(11223).sync();
    }

    @PreDestroy
    public void stop() throws InterruptedException {
        future.channel().closeFuture().sync();
    }
}
