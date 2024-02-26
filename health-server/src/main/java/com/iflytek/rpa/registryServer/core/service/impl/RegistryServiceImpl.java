package com.iflytek.rpa.registryServer.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.iflytek.rpa.registryServer.conf.RegistryException;
import com.iflytek.rpa.registryServer.core.entity.ClientInfo;
import com.iflytek.rpa.registryServer.core.entity.Instance;
import com.iflytek.rpa.registryServer.core.entity.ServiceManager;
import com.iflytek.rpa.registryServer.core.service.RegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.iflytek.rpa.registryServer.core.constant.Constances.LOG_PARAM_ILLEGAL;

@Service
public class RegistryServiceImpl implements RegistryService {
    
    @Autowired
    private ServiceManager serviceManager;

    /**
     * 服务注册
     * @param clientInfo
     * @return
     */
    @Override
    public String registry(ClientInfo clientInfo) throws RegistryException {
        String nameSpace = clientInfo.getNameSpace();
        String serviceName = clientInfo.getServiceName();

        if(null == nameSpace || null == serviceName){
            throw new IllegalArgumentException(LOG_PARAM_ILLEGAL);
        }
        final Instance instance = parseInstance(clientInfo);
        serviceManager.registerInstance(nameSpace, serviceName, instance);
        return "ok！";
    }

    private Instance parseInstance(ClientInfo clientInfo){
        Instance instance = new Instance();
        instance.setIp(clientInfo.getIp());
        instance.setPort(clientInfo.getPort());
        instance.setServiceName(clientInfo.getServiceName());
        instance.setLastBeat(System.currentTimeMillis());
        return instance;
    }
}