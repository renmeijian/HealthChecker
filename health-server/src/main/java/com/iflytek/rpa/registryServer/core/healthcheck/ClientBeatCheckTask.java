package com.iflytek.rpa.registryServer.core.healthcheck;

import com.alibaba.fastjson.JSON;
import com.iflytek.rpa.registryServer.core.entity.Instance;
import com.iflytek.rpa.registryServer.core.entity.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.iflytek.rpa.registryServer.core.constant.Constances.*;
import static com.iflytek.rpa.registryServer.net.NetUtils.alarmSend;

public class ClientBeatCheckTask implements Runnable {
    private final static Logger log = LoggerFactory.getLogger(ClientBeatCheckTask.class);
    private final Service service;
    public ClientBeatCheckTask(Service service) {
        this.service = service;
    }

    @Override
    public void run() {
        try {
            Set<Instance> instanceSet = service.getInstanceSet();
            for (Instance instance : instanceSet) {
                if (System.currentTimeMillis() - instance.getLastBeat() > TimeUnit.SECONDS.toMillis(TIMEOUT_UNHEALTHY)) {
                    if (instance.isHealthy()) {
                        synchronized (service){
                            instance.setHealthy(false);
                            alarmSend(instance.getServiceName(), SEND_LEVEL_SMS, instance.toIpAddr());
                            log.info(LOG_UNHEALTHY, instance.toIpAddr());
                        }
                    }
                }
            }
            for (Instance instance : instanceSet) {
                if (System.currentTimeMillis() - instance.getLastBeat() > TimeUnit.SECONDS.toMillis(TIMEOUT_DELETE)) {
                    synchronized (service){
                        // 从服务列表中同步删除
                        log.info(LOG_DELETE, service.getServiceName(), JSON.toJSONString(instance));
                        instanceSet.remove(instance);
                    }
                }
            }
        } catch (Exception e) {
            log.warn(LOG_BEAT_EXCEPTION, e);
        }
    }
}
