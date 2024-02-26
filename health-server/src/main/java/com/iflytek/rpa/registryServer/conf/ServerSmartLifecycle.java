package com.iflytek.rpa.registryServer.conf;

import com.iflytek.rpa.registryServer.core.entity.Instance;
import com.iflytek.rpa.registryServer.core.healthcheck.event.HealthEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ServerSmartLifecycle implements SmartLifecycle {

    @Autowired
    private ApplicationContext applicationContext;

    private volatile boolean running = false;

    private  List<Instance> configList = new ArrayList<>();

    /**
     * 容器启动后调用
     */
    @Override
    public void start() {
        running = true;
        init();
        applicationContext.publishEvent(new HealthEvent(this, configList));
    }

    /**
     * 将开启了健康检查的配置放到list中
     */
    public void init(){
        Map<String, Object> beanMap = applicationContext.getBeansWithAnnotation(ConfigInit.class);
        beanMap.values().forEach(healthConfig -> {
            Instance config = (Instance) healthConfig;
            if(config.isEnableCheck()){
                configList.add(config);
            }
        });
    }

    @Override
    public void stop() {
        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public int getPhase() {
        return SmartLifecycle.super.getPhase();
    }
}
