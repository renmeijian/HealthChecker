package com.iflytek.rpa.registryServer.core.service;


import com.iflytek.rpa.registryServer.conf.RegistryException;
import com.iflytek.rpa.registryServer.core.entity.ClientInfo;
import com.iflytek.rpa.registryServer.core.entity.Service;

import java.net.UnknownHostException;
import java.util.Map;

public interface RegistryService {

    String registry(ClientInfo clientInfo) throws RegistryException;

}
