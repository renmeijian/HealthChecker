package com.iflytek.rpa.registryServer.core.healthcheck.config;

import com.iflytek.rpa.registryServer.conf.ConfigInit;
import com.iflytek.rpa.registryServer.core.entity.Instance;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author mjren
 * @date 2023-11-27 10:42
 * @copyright Copyright (c) 2023 mjren
 */
@ConfigurationProperties(prefix = "health.etcd")
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@ConfigInit
public class HealthEtcdConfig extends Instance {
    private String type = "etcd";

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }
}
