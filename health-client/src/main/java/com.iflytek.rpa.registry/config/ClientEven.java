package com.iflytek.rpa.registry.config;

import com.iflytek.rpa.registry.core.entity.ServerInfo;
import org.springframework.context.ApplicationEvent;

/**
 * 事件
 */
public class ClientEven extends ApplicationEvent {

    private ServerInfo serverInfo;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public ClientEven(Object source, ServerInfo serverInfo) {
        super(source);
        this.serverInfo = serverInfo;
    }

    public ServerInfo getServerInfo() {
        return serverInfo;
    }

    public void setServerInfo(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }
}