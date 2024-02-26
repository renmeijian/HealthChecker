package com.iflytek.rpa.registryServer.core.healthcheck.config;

import com.iflytek.rpa.registryServer.conf.ConfigInit;
import com.iflytek.rpa.registryServer.core.entity.Instance;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "health.redis")
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@ConfigInit
public class HealthRedisConfig extends Instance {
    private String type = "redis";

    private String database;

    private String pwd;

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
