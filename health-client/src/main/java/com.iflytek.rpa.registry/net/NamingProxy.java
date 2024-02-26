package com.iflytek.rpa.registry.net;

import com.iflytek.rpa.registry.core.entity.BeatInfo;
import com.iflytek.rpa.registry.core.entity.Instance;
import com.iflytek.rpa.registry.core.entity.ServerInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static com.iflytek.rpa.registry.config.constants.*;

public class NamingProxy {
    private RestTemplate restTemplate;

    public NamingProxy() {
        this.restTemplate = new RestTemplate();
    }

    public ResponseEntity<String> sendBeat(ServerInfo serverInfo, BeatInfo beatInfo) {
        return restTemplate.postForEntity(HTTP + serverInfo.getIp() + COLON + serverInfo.getPort() + BEAT_URI, beatInfo, String.class);
    }
    public void registerInstance(ServerInfo serverInfo, Instance instance) {
        HashMap<String, String> map = new HashMap<>();
        map.put(IP, instance.getIp());
        map.put(PORT, instance.getPort());
        map.put(SERVICE_NAME, instance.getServiceName());
        map.put(NAME_SPACE, serverInfo.getNameSpace());
        ResponseEntity<String> entity = restTemplate.postForEntity(HTTP + serverInfo.getIp() + COLON + serverInfo.getPort() + REGISTRY_URI, map, String.class);
    }

}
