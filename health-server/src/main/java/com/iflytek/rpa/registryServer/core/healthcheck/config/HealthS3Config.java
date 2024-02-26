package com.iflytek.rpa.registryServer.core.healthcheck.config;

import com.iflytek.rpa.registryServer.conf.ConfigInit;
import com.iflytek.rpa.registryServer.core.entity.Instance;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author mjren
 * @date 2023-11-24 16:24
 * @copyright Copyright (c) 2023 mjren
 */
@ConfigurationProperties(prefix = "health.s3")
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@ConfigInit
public class HealthS3Config extends Instance {
    private String type = "s3";

    private String bucketName;

    private String prefix;

    private String accessKey;

    private String secretKey;

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
