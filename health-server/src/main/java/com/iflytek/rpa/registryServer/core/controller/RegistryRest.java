package com.iflytek.rpa.registryServer.core.controller;

import com.iflytek.rpa.registryServer.conf.RegistryException;
import com.iflytek.rpa.registryServer.core.entity.*;
import com.iflytek.rpa.registryServer.core.service.RegistryService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.iflytek.rpa.registryServer.core.constant.Constances.*;

@RestController
@RequestMapping("/rpa/registry")
public class RegistryRest {
    private final static Logger log = LoggerFactory.getLogger(RegistryRest.class);

    @Autowired
    private RegistryService registryService;

    @Autowired
    private ServiceManager serviceManager;

    @PostMapping("/setRegisterContext")
    public String registry(@RequestBody ClientInfo clientInfo) throws RegistryException {
        return registryService.registry(clientInfo);
    }

    @PostMapping("/beat")
    public String beat(@RequestBody ClientInfo clientInfo) throws RegistryException {
        String nameSpace = clientInfo.getNameSpace();
        String serviceName = clientInfo.getServiceName();
        if(null == nameSpace || null == serviceName){
            log.error(LOG_BEAT_NS_NULL);
        }
        String ip = clientInfo.getIp();
        String port = clientInfo.getPort();
        Instance instance = serviceManager.getInstance(nameSpace, serviceName, ip, port);
        if (instance == null) {
           log.warn(LOG_INSTANCE_NULL, clientInfo, serviceName);
            return RESOURCE_NOT_FOUND;
        }
        Service service = serviceManager.getService(nameSpace, serviceName);
        if (service == null) {
            throw new RegistryException(LOG_SERVER_ERROR,
                    LOG_SERVICE_NULL + serviceName  + nameSpace);
        }
        service.processClientBeat(clientInfo);
        return "ok";
    }
}