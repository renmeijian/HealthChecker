package com.iflytek.rpa.registry.core.entity;

import java.io.Serializable;

public class Instance implements Serializable {
    private static final long serialVersionUID = -742906310567291979L;
    /**
     * instance ip.
     */
    private String ip;

    /**
     * instance port.
     */
    private String port;


    /**
     * instance health status.
     */
    private boolean healthy = true;

    /**
     * Service information of instance.
     */
    private String serviceName;

    private String nameSpace;

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

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public String toInetAddr() {
        return ip + ":" + port;
    }
}
