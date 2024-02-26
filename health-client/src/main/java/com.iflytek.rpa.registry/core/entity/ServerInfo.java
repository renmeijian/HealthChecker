package com.iflytek.rpa.registry.core.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ConfigurationProperties：获取application.yml中的参数值
 */
@ConfigurationProperties(prefix = "health-server.info")
@NoArgsConstructor
@AllArgsConstructor
public class ServerInfo {
    private String ip;

    private String port;

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

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }
}