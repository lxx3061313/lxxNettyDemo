package com.lxx.mydemo.nettydemo.service.bean;

/**
 * @author lixiaoxiong
 * @version 2018-02-24
 */
public class DeviceJoinNetMsg {

    /**
     * 智能锁id, 8字节
     */
    private byte[] deviceId;

    /**
     * 电池电量
     */
    private byte batteryVol;

    public byte[] getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(byte[] deviceId) {
        this.deviceId = deviceId;
    }

    public byte getBatteryVol() {
        return batteryVol;
    }

    public void setBatteryVol(byte batteryVol) {
        this.batteryVol = batteryVol;
    }
}
