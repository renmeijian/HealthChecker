package com.iflytek.rpa.registryServer.core.healthcheck.processor;

import com.alibaba.fastjson.JSON;
import com.iflytek.rpa.registryServer.executor.GlobalExecutor;
import com.iflytek.rpa.registryServer.conf.HealthChecker;
import com.iflytek.rpa.registryServer.core.entity.Instance;
import com.iflytek.rpa.registryServer.core.healthcheck.HealthCheckTask;
import com.iflytek.rpa.registryServer.core.healthcheck.config.HealthEtcdConfig;
import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.kv.GetResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.iflytek.rpa.registryServer.core.constant.Constances.*;
import static com.iflytek.rpa.registryServer.net.NetUtils.alarmSend;

/**
 * @author mjren
 * @date 2023-11-27 11:10
 * @copyright Copyright (c) 2023 mjren
 */
@HealthChecker(type = ETCD)
@Component
public class EtcdHealthCheckProcessor implements HealthCheckProcessor{
    private final static Logger log = LoggerFactory.getLogger(EtcdHealthCheckProcessor.class);

    private static final ConcurrentMap<String, Client> CONNECTION_POOL = new ConcurrentHashMap<String, Client>();

    @Override
    public String getType() {
        return ETCD;
    }

    @Override
    public void process(HealthCheckTask task) {
        Set<Instance> configs = task.getService().getInstanceSet();
        if (CollectionUtils.isEmpty(configs)) {
            return;
        }
        for (Instance config : configs) {
            try {
                GlobalExecutor.executeCheckTask(new EtcdHealthCheckProcessor.EtcdCheckTask(config));
            } catch (Exception e) {
                log.error(LOG_CHECK_FAIL, getType(), e.getMessage());
            }
        }
    }

    private class EtcdCheckTask implements Runnable {
        private final HealthEtcdConfig etcdConfig;

        public EtcdCheckTask(Instance config) {
            this.etcdConfig = (HealthEtcdConfig) config;
        }

        @Override
        public void run() {
            Client etcdClient;
            String key = getType() + COLON + etcdConfig.getIp() + COLON + etcdConfig.getPort();
            try {
                etcdClient = CONNECTION_POOL.get(key);
                if (etcdClient == null) {
                    String url = HTTP + etcdConfig.getIp() + COLON + etcdConfig.getPort();
                    etcdClient = Client.builder().endpoints(url).build();
                    CONNECTION_POOL.put(key, etcdClient);
                }
                ByteSequence etcdkey = ByteSequence.from(ETCD_KEY.getBytes());
                GetResponse response = etcdClient.getKVClient().get(etcdkey).get();
                log.debug(LOG_HEALTHY, getType(), JSON.toJSONString(response));
            } catch (Throwable t) {
                alarmSend(getType(), SEND_LEVEL_SMS, key);
                log.error(LOG_CONNECT_ERROR, getType(),  t.getMessage());
            }
        }
    }
}
