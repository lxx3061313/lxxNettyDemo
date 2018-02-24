package com.lxx.mydemo.nettydemo.service.impl;

import com.lxx.mydemo.nettydemo.service.bean.P808Msg.P808MsgHeader;
import com.lxx.mydemo.nettydemo.service.bean.P808MsgType;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author lixiaoxiong
 * @version 2018-02-24
 */
@Service
public class DeviceLeaveNetReportMsgReport extends AbstractMsgProcessor {
    private final static Logger logger = LoggerFactory.getLogger(DeviceLeaveNetReportMsgReport.class);

    @Override
    void doProcess(P808MsgHeader header, byte[] body, ChannelHandlerContext ctx) {
        logger.info("设备脱网报告");
    }

    @Override
    int msgCode() {
        return P808MsgType.DEVICE_LEAVE_TERMINAL_NET.getCode();
    }

    @Override
    void postProcess(P808MsgHeader header, byte[] body, ChannelHandlerContext ctx) {

    }
}
