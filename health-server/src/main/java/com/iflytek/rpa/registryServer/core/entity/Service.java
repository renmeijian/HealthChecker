package com.iflytek.rpa.registryServer.core.entity;

import com.iflytek.rpa.registryServer.executor.HealthCheckReactor;
import com.iflytek.rpa.registryServer.core.healthcheck.ClientBeatCheckTask;
import com.iflytek.rpa.registryServer.core.healthcheck.processor.ClientBeatProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

import static com.iflytek.rpa.registryServer.core.constant.Constances.LOG_CHANGE;

public class Service {

    private final static Logger log = LoggerFactory.getLogger(Service.class);
    private Set<Instance> instanceSet;

    private String serviceName;

    private String nameSpace;

    private String type;

    public Set<Instance> getInstanceSet() {
        return instanceSet;
    }

    public void setInstanceSet(Set<Instance> instanceSet) {
        this.instanceSet = instanceSet;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private ClientBeatCheckTask clientBeatCheckTask = new ClientBeatCheckTask(this);


    public void init() {
        //在这里启动心跳健康检查，5秒一次
        HealthCheckReactor.scheduleCheck(clientBeatCheckTask);
    }


    public void onChange(Instances instances) {
        log.info(LOG_CHANGE, serviceName);
        instanceSet = new HashSet<>(instances.getInstanceList());
    }

    public void processClientBeat(final ClientInfo clientInfo) {
        ClientBeatProcessor clientBeatProcessor = new ClientBeatProcessor();
        clientBeatProcessor.setService(this);
        clientBeatProcessor.setClientInfo(clientInfo);
        HealthCheckReactor.scheduleNow(clientBeatProcessor);
    }

}
