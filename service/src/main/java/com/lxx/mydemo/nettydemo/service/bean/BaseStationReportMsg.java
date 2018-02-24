package com.lxx.mydemo.nettydemo.service.bean;

import java.util.Date;

/**
 * @author lixiaoxiong
 * @version 2018-02-24
 */
public class BaseStationReportMsg {

    /**
     * 报警标志(4字节), 固定为0
     */
    private int alartFlag;


    /**
     * 状态(4字节), 固定为0
     */
    private int status;


    /**
     * 纬度(4字节), 以度为单位的纬度值乘以10的6次方,精确到百万分之一度。
     */
    private int latitude;


    /**
     * 经度(4字节),同上
     */
    private int longitude;

    /**
     * 高度(2字节),单位米
     */
    private int height;

    /**
     * 速度(2字节), 固定为0
     */
    private int speed;


    /**
     * 方向(2字节), 固定为0
     */
    private int direction;

    /**
     * BCD[6], YY-MM-DD-hh-mm-ss
     */
    private Date date;

    /**
     * 在线车锁总量
     */
    private int  onlineDeviceNum;

    /**
     * 剩余可停车数量
     */
    private int partableNum;


    /**
     * 电量 0-100
     */
    private int batteryLevel;


    /**
     * 是否有外接电源
     */
    private boolean exPower;

    /**
     * 是否已停止充电(充满)
     */
    private boolean stopCharge;

    /**
     * 外接电源电压值(单位0.001V)
     */
    private double exVoltage;

    /**
     * 电池电压值(单位0.001V)
     */
    private double batteryVoltage;


    public int getAlartFlag() {
        return alartFlag;
    }

    public void setAlartFlag(int alartFlag) {
        this.alartFlag = alartFlag;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getOnlineDeviceNum() {
        return onlineDeviceNum;
    }

    public void setOnlineDeviceNum(int onlineDeviceNum) {
        this.onlineDeviceNum = onlineDeviceNum;
    }

    public int getPartableNum() {
        return partableNum;
    }

    public void setPartableNum(int partableNum) {
        this.partableNum = partableNum;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public boolean isExPower() {
        return exPower;
    }

    public void setExPower(boolean exPower) {
        this.exPower = exPower;
    }

    public boolean isStopCharge() {
        return stopCharge;
    }

    public void setStopCharge(boolean stopCharge) {
        this.stopCharge = stopCharge;
    }

    public double getExVoltage() {
        return exVoltage;
    }

    public void setExVoltage(double exVoltage) {
        this.exVoltage = exVoltage;
    }

    public double getBatteryVoltage() {
        return batteryVoltage;
    }

    public void setBatteryVoltage(double batteryVoltage) {
        this.batteryVoltage = batteryVoltage;
    }
}
