package com.iflytek.rpa.registryServer.core.entity;

import static com.iflytek.rpa.registryServer.core.constant.Constances.COLON;

public class Instance {
    private String ip;

    private String port;

    private boolean healthy = true;

    private String serviceName;

    private volatile long lastBeat = System.currentTimeMillis();

    public String type;

    private boolean enableCheck = true;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public boolean isHealthy() {
        return healthy;
    }

    public void setHealthy(boolean healthy) {
        this.healthy = healthy;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public long getLastBeat() {
        return lastBeat;
    }

    public void setLastBeat(long lastBeat) {
        this.lastBeat = lastBeat;
    }

    public String toIpAddr() {
        return getIp() + COLON + getPort();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isEnableCheck() {
        return enableCheck;
    }

    public void setEnableCheck(boolean enableCheck) {
        this.enableCheck = enableCheck;
    }
}
