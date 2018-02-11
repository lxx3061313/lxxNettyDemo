package com.lxx.mydemo.nettydemo.service.common.net;

import com.sun.org.apache.regexp.internal.RE;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lixiaoxiong
 * @version 2018-02-11
 */
@Configuration
public class NettySpringConfig {
    private final static Logger logger = LoggerFactory.getLogger(NettySpringConfig.class);

    @Value("${boss.thread.count}")
    private int bossCount;

    @Value("${worker.thread.count}")
    private int workerCount;

    @Bean(name = "serverBootstrap")
    public ServerBootstrap bootstrap() {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup(), workerGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg)
                                    throws Exception {
                                ByteBuf buf = (ByteBuf) msg;
                                byte[] bytes = new byte[buf.readableBytes()];
                                buf.readBytes(bytes);
                                String body = new String(bytes, "UTF-8");
                                logger.info("receive msg:{}", body);
                            }
                        });
                    }
                }).option(ChannelOption.SO_BACKLOG, 1024);
        return bootstrap;
    }

    @Bean(name = "bossGroup")
    public NioEventLoopGroup bossGroup() {
        return new NioEventLoopGroup(bossCount);
    }

    @Bean(name= "workerGroup")
    public NioEventLoopGroup workerGroup() {
        return new NioEventLoopGroup(workerCount);
    }
}