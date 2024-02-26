package com.iflytek.rpa.registry.core;

import com.iflytek.rpa.registry.core.beat.BeatReactor;
import com.iflytek.rpa.registry.core.entity.BeatInfo;
import com.iflytek.rpa.registry.config.ClientEven;
import com.iflytek.rpa.registry.core.entity.Instance;
import com.iflytek.rpa.registry.core.entity.ServerInfo;
import com.iflytek.rpa.registry.net.NamingProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.iflytek.rpa.registry.config.constants.*;

/**
 * 服务注册的事件监听
 */
//@Slf4j
public class ClientRegistry {
    private final static Logger log = LoggerFactory.getLogger(ClientRegistry.class);
    @Autowired
    Environment environment;

    private NamingProxy namingProxy;

    private BeatReactor beatReactor;

    public static final int DEFAULT_CLIENT_BEAT_THREAD_COUNT =
            Runtime.getRuntime().availableProcessors() > 1 ? Runtime.getRuntime().availableProcessors() / 2 : 1;

    public ClientRegistry(){
        this.namingProxy = new NamingProxy();
        this.beatReactor = new BeatReactor(this.namingProxy, DEFAULT_CLIENT_BEAT_THREAD_COUNT);
    }


    @EventListener
    public void registry(ClientEven even) {
        Instance instance = buildInstance(even.getServerInfo());
        BeatInfo beatInfo = beatReactor.buildBeatInfo(instance);
        beatReactor.addBeatInfo(even.getServerInfo(), beatInfo);
        try {
            namingProxy.registerInstance(even.getServerInfo(), instance);
        }catch (Exception e){
            log.error(LOG_REGISTRY_ERROR, e.getMessage());
            e.printStackTrace();
        }

    }


    private String getIp(){
        String ip = null;
        try{
            ip = InetAddress.getLocalHost().getHostAddress();
        }catch (UnknownHostException e){
            e.printStackTrace();
        }
        return ip;
    }

    private Instance buildInstance(ServerInfo serverInfo){
        Instance instance = new Instance();
        String ip = getIp();
        String port = environment.getProperty(LOG_SERVER_PORT);
        String serviceName = environment.getProperty(LOG_APPLICATION_NAME);
        instance.setIp(ip);
        instance.setPort(port);
        instance.setServiceName(serviceName);
        instance.setNameSpace(serverInfo.getNameSpace());
        return instance;
    }
}
