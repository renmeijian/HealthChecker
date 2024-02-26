package com.iflytek.rpa.registryServer.core.healthcheck.processor;

import com.iflytek.rpa.registryServer.executor.GlobalExecutor;
import com.iflytek.rpa.registryServer.conf.HealthChecker;
import com.iflytek.rpa.registryServer.core.entity.Instance;
import com.iflytek.rpa.registryServer.core.healthcheck.HealthCheckTask;
import com.iflytek.rpa.registryServer.core.healthcheck.config.HealthRedisConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.iflytek.rpa.registryServer.core.constant.Constances.*;
import static com.iflytek.rpa.registryServer.net.NetUtils.alarmSend;

/**
 * @author mjren
 * @date 2023-11-23 18:43
 * @copyright Copyright (c) 2023 mjren
 */
@HealthChecker(type = REDIS)
@Component
public class RedisHealthCheckProcessor implements HealthCheckProcessor{
    private final static Logger log = LoggerFactory.getLogger(RedisHealthCheckProcessor.class);

    private static final ConcurrentMap<String, Jedis> CONNECTION_POOL = new ConcurrentHashMap<String, Jedis>();

    @Override
    public String getType() {
        return REDIS;
    }

    @Override
    public void process(HealthCheckTask task) {
        Set<Instance> configs = task.getService().getInstanceSet();
        if (CollectionUtils.isEmpty(configs)) {
            return;
        }
        for (Instance config : configs) {
            try {
                GlobalExecutor.executeCheckTask(new RedisCheckTask(config));
            } catch (Exception e) {
                log.error(LOG_CHECK_FAIL, getType(), e.getMessage());
            }
        }
    }

    private class RedisCheckTask implements Runnable {
        private final HealthRedisConfig redisConfig;

        public RedisCheckTask(Instance config) {
            this.redisConfig = (HealthRedisConfig) config;
        }

        @Override
        public void run() {
            Jedis jedis;
            String key = REDIS + COLON + redisConfig.getIp() + COLON + redisConfig.getPort();
            try {
                jedis = CONNECTION_POOL.get(key);
                if (jedis == null || jedis.isBroken()) {
                    jedis = new Jedis(redisConfig.getIp(), Integer.parseInt(redisConfig.getPort()));
                    jedis.auth(redisConfig.getPwd());
                    CONNECTION_POOL.put(key, jedis);
                }
                Boolean exists = jedis.exists(REDIS_KEY);
                log.debug(LOG_HEALTHY,getType(), exists);
            } catch (Throwable t) {
                alarmSend(getType(), SEND_LEVEL_SMS, key);
                log.error(LOG_CONNECT_ERROR, getType(),  t.getMessage());
            }
        }
    }

}
