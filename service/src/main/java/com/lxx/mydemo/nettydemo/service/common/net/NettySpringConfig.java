package com.lxx.mydemo.nettydemo.service.common.net;

import com.lxx.mydemo.nettydemo.service.bean.MsgConstants;
import com.lxx.mydemo.nettydemo.service.p808.P808Decoder;
import com.lxx.mydemo.nettydemo.service.p808.P808Encoder;
import com.lxx.mydemo.nettydemo.service.p808.TerminalAuthHandler;
import com.lxx.mydemo.nettydemo.service.p808.TerminalRegHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import java.util.concurrent.TimeUnit;
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
                        // 空闲检测
                        ch.pipeline().addLast("idleStateHandler", new IdleStateHandler(15, 0, 0,
                                TimeUnit.MINUTES));

                        // 半包/粘包分解器
                        ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, Unpooled.copiedBuffer(new byte[] {
                                MsgConstants.MSG_IDENTIFICATION_BIT }),
                                Unpooled.copiedBuffer(new byte[] { MsgConstants.MSG_IDENTIFICATION_BIT,
                                        MsgConstants.MSG_IDENTIFICATION_BIT })));

                        // 808协议解码器
                        ch.pipeline().addLast(p808MsgDecoder());

                        // 808协议编码器
                        ch.pipeline().addLast(p808Encoder());

                        // 终端注册处理器
                        ch.pipeline().addLast(terminalRegHandler());

                        // 终端鉴权处理器
                        ch.pipeline().addLast(terminalAuthHandler());
                    }
                }).option(ChannelOption.SO_BACKLOG, 1024);
        return bootstrap;
    }

    @Bean(name = "bossGroup")
    public NioEventLoopGroup bossGroup() {
        return new NioEventLoopGroup(bossCount);
    }

    @Bean(name = "workerGroup")
    public NioEventLoopGroup workerGroup() {
        return new NioEventLoopGroup(workerCount);
    }

    @Bean(name = "p808Decoder")
    public P808Decoder p808MsgDecoder() {
        return new P808Decoder();
    }

    @Bean(name = "terminalRegHandler")
    public TerminalRegHandler terminalRegHandler() {
        return new TerminalRegHandler();
    }

    @Bean(name = "terminalAuthHandler")
    public TerminalAuthHandler terminalAuthHandler() {
        return new TerminalAuthHandler();
    }

    @Bean(name = "p808Encoder")
    public P808Encoder p808Encoder() {
        return new P808Encoder();
    }
}
