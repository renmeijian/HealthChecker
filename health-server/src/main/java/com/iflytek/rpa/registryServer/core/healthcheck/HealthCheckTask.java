package com.iflytek.rpa.registryServer.core.healthcheck;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iflytek.rpa.registryServer.core.entity.Service;
import com.iflytek.rpa.registryServer.core.healthcheck.processor.HealthCheckProcessor;
import com.iflytek.rpa.registryServer.core.healthcheck.processor.HealthCheckProcessorDelegate;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import static com.iflytek.rpa.registryServer.core.constant.Constances.LOG_HEALTHY_ERROR;
@Data
public class HealthCheckTask implements  Runnable {
    private final static Logger log = LoggerFactory.getLogger(HealthCheckTask.class);

    private Service service;

    private String type;

    @JsonIgnore
    private HealthCheckProcessor healthCheckProcessor;

    public HealthCheckTask(Service service,ApplicationContext applicationContext) {
        this.service = service;
        this.type = service.getType();
        healthCheckProcessor = applicationContext.getBean(HealthCheckProcessorDelegate.class);
    }

    @Override
    public void run() {
        try {
            healthCheckProcessor.process(this);
        } catch (Throwable e) {
            log.error(LOG_HEALTHY_ERROR,service.getServiceName(), e);
        }
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
