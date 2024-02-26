package com.iflytek.rpa.registryServer.core.healthcheck.processor;

import com.iflytek.rpa.registryServer.conf.HealthChecker;
import com.iflytek.rpa.registryServer.core.healthcheck.HealthCheckTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

import static com.iflytek.rpa.registryServer.core.constant.Constances.LOG_NO_PROCESSOR;

@Component
public class HealthCheckProcessorDelegate implements HealthCheckProcessor {
    private final static Logger log = LoggerFactory.getLogger(HealthCheckProcessorDelegate.class);

    @Resource
    private ApplicationContext applicationContext;
    private final Map<String, HealthCheckProcessor> healthCheckProcessorMap = new HashMap<>();

    @PostConstruct
    public void init(){
        Map<String, Object> beanMap = applicationContext.getBeansWithAnnotation(HealthChecker.class);
        beanMap.values().forEach(processor -> {
            HealthChecker annotation = processor.getClass().getAnnotation(HealthChecker.class);
            healthCheckProcessorMap.put(annotation.type(), (HealthCheckProcessor) processor);
        });
    }

    @Override
    public void process(HealthCheckTask task) {
        String type = task.getType();
        HealthCheckProcessor processor = healthCheckProcessorMap.get(type);
        if (processor == null) {
            log.error(LOG_NO_PROCESSOR);
            return;
        }
        processor.process(task);
    }

    @Override
    public String getType() {
        return null;
    }
}
