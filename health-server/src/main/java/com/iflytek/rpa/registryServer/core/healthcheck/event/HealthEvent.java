package com.iflytek.rpa.registryServer.core.healthcheck.event;

import com.iflytek.rpa.registryServer.core.entity.Instance;
import com.iflytek.rpa.registryServer.core.healthcheck.config.HealthEtcdConfig;
import com.iflytek.rpa.registryServer.core.healthcheck.config.HealthMysqlConfig;
import com.iflytek.rpa.registryServer.core.healthcheck.config.HealthRedisConfig;
import com.iflytek.rpa.registryServer.core.healthcheck.config.HealthS3Config;
import org.springframework.context.ApplicationEvent;

import java.util.ArrayList;
import java.util.List;

public class HealthEvent extends ApplicationEvent {
    private List<Instance> configList;

    public HealthEvent(Object source, List<Instance> configList) {
        super(source);
        this.configList = configList;
    }

    public List<Instance> getConfigList() {
        return configList;
    }

    public void setConfigList(List<Instance> configList) {
        this.configList = configList;
    }
}
