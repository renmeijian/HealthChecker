package com.iflytek.rpa.registry.core.beat;

import com.alibaba.fastjson.JSON;
import com.iflytek.rpa.registry.core.entity.BeatInfo;
import com.iflytek.rpa.registry.core.entity.Instance;
import com.iflytek.rpa.registry.core.entity.ServerInfo;
import com.iflytek.rpa.registry.net.NamingProxy;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import static com.iflytek.rpa.registry.config.constants.*;

public class BeatReactor {
    private final static Logger log = LoggerFactory.getLogger(BeatReactor.class);

    private final NamingProxy namingProxy;

    private final ScheduledExecutorService executorService;

    public final long DEFAULT_HEART_BEAT_INTERVAL = TimeUnit.SECONDS.toMillis(5);


    public BeatReactor(NamingProxy namingProxy, int threadCount) {
        this.namingProxy = namingProxy;
        this.executorService = new ScheduledThreadPoolExecutor(threadCount, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                thread.setName(THREAD_SENDER);
                return thread;
            }
        });
    }

    public BeatInfo buildBeatInfo(Instance instance) {
        BeatInfo beatInfo = new BeatInfo();

        beatInfo.setIp(instance.getIp());
        beatInfo.setPort(instance.getPort());
        beatInfo.setServiceName(instance.getServiceName());
        beatInfo.setNameSpace(instance.getNameSpace());
        beatInfo.setPeriod(DEFAULT_HEART_BEAT_INTERVAL);
        return beatInfo;
    }

    public void addBeatInfo(ServerInfo serverInfo, BeatInfo beatInfo) {
        executorService.schedule(new BeatTask(serverInfo, beatInfo), beatInfo.getPeriod(), TimeUnit.MILLISECONDS);
    }

    class BeatTask implements Runnable {

        private ServerInfo serverInfo;

        private BeatInfo beatInfo;

        public BeatTask(ServerInfo serverInfo, BeatInfo beatInfo) {
            this.beatInfo = beatInfo;
            this.serverInfo = serverInfo;
        }

        @Override
        public void run() {
            long nextTime = beatInfo.getPeriod();
            try {
                ResponseEntity<String> entity = namingProxy.sendBeat(serverInfo, beatInfo);
                if (entity.getStatusCode() == HttpStatus.OK) {
                    String body = entity.getBody();
                    if(RESOURCE_NOT_FOUND.equals(body)){
                        Instance instance = new Instance();
                        instance.setPort(beatInfo.getPort());
                        instance.setIp(beatInfo.getIp());
                        instance.setServiceName(beatInfo.getServiceName());
                        instance.setNameSpace(beatInfo.getNameSpace());
                        try {
                            namingProxy.registerInstance(serverInfo, instance);
                        } catch (Exception e) {
                            log.error(LOG_BEAT_REGIST_ERROR, e.getMessage());
                        }
                    }
                }
            } catch (Exception ex) {
                log.error(LOG_BEAT_SEND_ERROR, JSON.toJSONString(beatInfo), ex);
            } finally {
                executorService.schedule(new BeatTask(serverInfo, beatInfo), nextTime, TimeUnit.MILLISECONDS);
            }
        }
    }
}
