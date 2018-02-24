package com.lxx.mydemo.nettydemo.service.p808;

import com.lxx.mydemo.nettydemo.service.bean.BaseStationReportMsg;
import com.lxx.mydemo.nettydemo.service.common.util.BCD8421Operater;
import com.lxx.mydemo.nettydemo.service.common.util.BitOperator;
import java.util.Date;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

/**
 * @author lixiaoxiong
 * @version 2018-02-24
 */
@Service
public class TerminalReportMsgDecoder {

    private final static double VOL_UNIT = 0.001;

    public BaseStationReportMsg decode(byte [] body) {
        if (body.length == 0) {
            throw new IllegalArgumentException("消息体内容异常,无法解码定时汇报消息");
        }

        BaseStationReportMsg msg = new BaseStationReportMsg();

        //报警标志
        msg.setAlartFlag(BitOperator.parseIntFromBytes(body, 0, 4));

        //状态
        msg.setStatus(BitOperator.parseIntFromBytes(body, 4, 4));

        //纬度
        msg.setLatitude(BitOperator.parseIntFromBytes(body, 8, 4));

        //经度
        msg.setLongitude(BitOperator.parseIntFromBytes(body, 12, 4));

        //高度
        msg.setHeight(BitOperator.parseIntFromBytes(body, 16, 2));

        //速度
        msg.setSpeed(BitOperator.parseIntFromBytes(body, 18, 2));

        //方向
        msg.setDirection(BitOperator.parseIntFromBytes(body, 20, 2));

        int year = BCD8421Operater.decIntFromBcd1(body[22]) + 2000;
        int mon = BCD8421Operater.decIntFromBcd1(body[23]);
        int day = BCD8421Operater.decIntFromBcd1(body[24]);
        int hour = BCD8421Operater.decIntFromBcd1(body[25]);
        int min = BCD8421Operater.decIntFromBcd1(body[26]);
        int sec = BCD8421Operater.decIntFromBcd1(body[27]);
        msg.setDate(pareseDate(year, mon, day, hour, min, sec));

        /**
         * 在线车锁数量
         */
        msg.setOnlineDeviceNum(BitOperator.parseIntFromBytes(body, 30, 2));

        /**
         * 剩余可停车数量
         */
        msg.setPartableNum(BitOperator.parseIntFromBytes(body,34, 2));

        /**
         * 电量
         */
        msg.setBatteryLevel(BitOperator.parseIntFromBytes(body, 38, 1));

        byte exPowerinfo = body[39];
        msg.setExPower((exPowerinfo & 0x01) == 1);
        msg.setStopCharge((exPowerinfo & 0x02) == 1);

        msg.setExVoltage(BitOperator.parseIntFromBytes(body, 40, 2) * VOL_UNIT);
        msg.setBatteryVoltage(BitOperator.parseIntFromBytes(body, 44, 2) * VOL_UNIT);
        return msg;
    }

    private Date pareseDate(int year, int mon, int day, int hour, int min, int sec) {
        Date current = new Date();
        DateUtils.setYears(current, year);
        DateUtils.setMonths(current, mon);
        DateUtils.setDays(current, day);
        DateUtils.setHours(current, hour);
        DateUtils.setMinutes(current, min);
        DateUtils.setSeconds(current, sec);
        return current;
    }
}
