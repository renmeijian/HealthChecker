package com.iflytek.rpa.registryServer.core.healthcheck.processor;

import com.iflytek.rpa.registryServer.core.entity.ClientInfo;
import com.iflytek.rpa.registryServer.core.entity.Instance;
import com.iflytek.rpa.registryServer.core.entity.Service;
import lombok.Data;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
@Data
public class ClientBeatProcessor implements Runnable  {

    private ClientInfo clientInfo;

    private Service service;

    private ApplicationContext applicationContext;

    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    /**
     * 找到心跳的这个实例，更新实例的最后一次心跳时间 lastBeat
     */
    @Override
    public void run() {
        Service service = this.service;
        String ip = clientInfo.getIp();
        String port = clientInfo.getPort();
        List<Instance> instanceList = new ArrayList<>(service.getInstanceSet());
        for (Instance instance : instanceList) {
            if (instance.getIp().equals(ip) && instance.getPort().equals(port)) {
                instance.setLastBeat(System.currentTimeMillis());
                if (!instance.isHealthy()) {
                    instance.setHealthy(true);
                }
            }
        }
    }
}
