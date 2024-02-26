package com.iflytek.rpa.registry.config;

import com.iflytek.rpa.registry.core.entity.ServerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.SmartLifecycle;

/**
 * 服务注册的事件发布
 */
public class ClientSmartLifecycle implements SmartLifecycle {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ServerInfo serverInfo;

    private volatile boolean running = false;

    /**
     * 容器启动后调用
     */
    @Override
    public void start() {
        System.out.println("smartLifecycle 容器启动完成 ...");
        running = true;
        // 发布注册事件
        applicationContext.publishEvent(new ClientEven(this, serverInfo));
    }

    /**
     * 容器停止时调用
     */
    @Override
    public void stop() {
        System.out.println("smartLifecycle stop 容器停止 ...");
        running = false;
    }

    @Override
    public boolean isRunning() {
        System.out.println("smartLifecycle 检查运行状态 ...");
        return running;
    }

    @Override
    public int getPhase() {
        return SmartLifecycle.super.getPhase();
    }
}
