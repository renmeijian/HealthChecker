package com.iflytek.rpa.registryServer.core.entity;

/**
 * @author mjren
 * @date 2023-11-28 16:32
 * @copyright Copyright (c) 2023 mjren
 */
public class SendDo {
    private String type;

    private String alarmInfo;

    private String sendType;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAlarmInfo() {
        return alarmInfo;
    }

    public void setAlarmInfo(String alarmInfo) {
        this.alarmInfo = alarmInfo;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }
}
