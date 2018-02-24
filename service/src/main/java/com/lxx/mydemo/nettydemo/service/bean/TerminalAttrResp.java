package com.lxx.mydemo.nettydemo.service.bean;

/**
 * @author lixiaoxiong
 * @version 2018-02-24
 */
public class TerminalAttrResp {

    /**
     * 终端类型, 固定为0x0000
     */
    private int terminalType;

    /**
     * 制造商id, 5字节
     */
    private byte[] makerId;

    /**
     * 终端型号,20字节
     */
    private byte[] terminalModel;

    /**
     * 终端id, 7字节,不足补0x00
     */
    private byte[] terminalId;


    /**
     * bcd[10]
     */
    private byte[] iccid;

    /**
     * 硬件版本
     */
    private String hardwardVersion;

    /**
     * 固件版本
     */
    private String firmwareVersion;

    public int getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(int terminalType) {
        this.terminalType = terminalType;
    }

    public byte[] getMakerId() {
        return makerId;
    }

    public void setMakerId(byte[] makerId) {
        this.makerId = makerId;
    }

    public byte[] getTerminalModel() {
        return terminalModel;
    }

    public void setTerminalModel(byte[] terminalModel) {
        this.terminalModel = terminalModel;
    }

    public byte[] getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(byte[] terminalId) {
        this.terminalId = terminalId;
    }

    public byte[] getIccid() {
        return iccid;
    }

    public void setIccid(byte[] iccid) {
        this.iccid = iccid;
    }

    public String getHardwardVersion() {
        return hardwardVersion;
    }

    public void setHardwardVersion(String hardwardVersion) {
        this.hardwardVersion = hardwardVersion;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }
}
