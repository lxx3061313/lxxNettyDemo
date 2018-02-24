package com.lxx.mydemo.nettydemo.service.impl;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.lxx.mydemo.nettydemo.service.bean.P808Msg;
import com.lxx.mydemo.nettydemo.service.bean.TerminalParamType;
import com.lxx.mydemo.nettydemo.service.p808.P808ConnFlowIdManager;
import com.lxx.mydemo.nettydemo.service.p808.P808SessionManager;
import com.lxx.mydemo.nettydemo.service.p808.msgbuilder.QueryTerminalAttrMsgBuilder;
import com.lxx.mydemo.nettydemo.service.p808.msgbuilder.QueryTerminalParamMsgBuilder;
import com.lxx.mydemo.nettydemo.service.p808.msgbuilder.TerminalParamQueryParamBuilder;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

/**
 * @author lixiaoxiong
 * @version 2018-02-24
 */
@Service
public class TerminalQueryService {
    private final static Logger logger = LoggerFactory.getLogger(TerminalQueryService.class);

    @Resource
    P808SessionManager p808SessionManager;

    @Resource
    P808ConnFlowIdManager p808ConnFlowIdManager;

    public void queryTerminalParam(String terminalId) {
        if (Strings.isNullOrEmpty(terminalId)) {
            return;
        }

        NioSocketChannel channel = p808SessionManager.getChannel(terminalId);
        if (channel == null) {
            logger.error("没有可用的通信channel");
            return;
        }

        P808Msg build = QueryTerminalParamMsgBuilder
                .createBuilder()
                .setTermimalId(terminalId)
                .setFlowId(p808ConnFlowIdManager.getFlowId(channel.id().asLongText()))
                .build();
        channel.writeAndFlush(build);
    }

    public void queryExactTerminalParam(String terminalId, String ids) {
        if (Strings.isNullOrEmpty(terminalId)) {
            return;
        }

        NioSocketChannel channel = p808SessionManager.getChannel(terminalId);
        if (channel == null) {
            logger.error("没有可用的通信channel");
            return;
        }

        ArrayList<String> idstrs = Lists.newArrayList(Splitter.on(":").split(ids));
        List<Integer> idis = idstrs.stream().map(p -> Integer.valueOf(p))
                .collect(Collectors.toList());
        TerminalParamQueryParamBuilder builder = TerminalParamQueryParamBuilder.createBuilder();
        for (Integer i : idis) {
            builder.setCode(TerminalParamType.codeOf(i).getCode());
        }

        builder.setTermimalId(terminalId)
                .setFlowId(p808ConnFlowIdManager.getFlowId(channel.id().asLongText()));
        channel.writeAndFlush(builder.build());
    }

    public void queryTerminalAttri(String terminalId) {
        if (Strings.isNullOrEmpty(terminalId)) {
            return;
        }

        NioSocketChannel channel = p808SessionManager.getChannel(terminalId);
        if (channel == null) {
            logger.error("没有可用的通信channel");
            return;
        }

        P808Msg build = QueryTerminalAttrMsgBuilder.createBuilder()
                .setTermimalId(terminalId)
                .setFlowId(p808ConnFlowIdManager.getFlowId(channel.id().asLongText()))
                .build();
        channel.writeAndFlush(build);
    }
}
