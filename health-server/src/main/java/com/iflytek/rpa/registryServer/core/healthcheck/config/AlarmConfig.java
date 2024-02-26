package com.iflytek.rpa.registryServer.core.healthcheck.config;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author mjren
 * @date 2023-11-28 14:57
 * @copyright Copyright (c) 2023 mjren
 */
@ConfigurationProperties(prefix = "health.alarm")
@NoArgsConstructor
@AllArgsConstructor
@Configuration
public class AlarmConfig {
    private String ip;

    private String port;

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
}
