package com.lxx.mydemo.nettydemo.service.common.net;

import com.lxx.mydemo.nettydemo.service.bean.MsgConstants;
import com.lxx.mydemo.nettydemo.service.common.util.ByteUtil;
import com.lxx.mydemo.nettydemo.service.common.util.DateFormatUtil;
import com.lxx.mydemo.nettydemo.service.p808.P808Decoder;
import com.sun.org.apache.regexp.internal.RE;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
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

    @Bean(name= "p808Decoder")
    public P808Decoder p808MsgDecoder() {
        return new P808Decoder();
    }
}
