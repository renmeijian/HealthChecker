package com.iflytek.rpa.registryServer.core.healthcheck.processor;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.iflytek.rpa.registryServer.executor.GlobalExecutor;
import com.iflytek.rpa.registryServer.conf.HealthChecker;
import com.iflytek.rpa.registryServer.core.entity.Instance;
import com.iflytek.rpa.registryServer.core.healthcheck.HealthCheckTask;
import com.iflytek.rpa.registryServer.core.healthcheck.config.HealthS3Config;
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
 * @date 2023-11-24 16:14
 * @copyright Copyright (c) 2023 mjren
 */
@HealthChecker(type = S3)
@Component
public class S3HealthCheckProcessor implements HealthCheckProcessor{
    private final static Logger log = LoggerFactory.getLogger(S3HealthCheckProcessor.class);

    private static final ConcurrentMap<String, AmazonS3> CONNECTION_POOL = new ConcurrentHashMap<String, AmazonS3>();

    @Override
    public String getType() {
        return S3;
    }

    @Override
    public void process(HealthCheckTask task) {
        Set<Instance> configs = task.getService().getInstanceSet();
        if (CollectionUtils.isEmpty(configs)) {
            return;
        }
        for (Instance config : configs) {
            try {
                GlobalExecutor.executeCheckTask(new S3CheckTask(config));
            } catch (Exception e) {
                log.error(LOG_CHECK_FAIL, getType(), e.getMessage());
            }
        }
    }

    private class S3CheckTask implements Runnable {
        private final HealthS3Config s3Config;

        public S3CheckTask(Instance config) {
            this.s3Config = (HealthS3Config) config;
        }

        @Override
        public void run() {
            AmazonS3 s3Client;
            String key = S3 + COLON + s3Config.getIp() + COLON + s3Config.getPort();
            try {
                s3Client = CONNECTION_POOL.get(key);
                if (s3Client == null) {
                    ClientConfiguration clientConf = new ClientConfiguration();
                    clientConf.setProtocol(Protocol.HTTP);
                    s3Client = AmazonS3ClientBuilder.standard()
                            .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(s3Config.getAccessKey(), s3Config.getSecretKey())))
                            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(s3Config.getIp(), Regions.US_EAST_1.getName()))
                            .withClientConfiguration(clientConf)
                            .build();

                    CONNECTION_POOL.put(key, s3Client);
                }
                boolean result = s3Client.doesBucketExistV2(s3Config.getBucketName());
                log.debug(LOG_HEALTHY,getType(), result);
            } catch (AmazonS3Exception e) {
                log.error(LOG_EXECUTE_ERROR, getType(), e.getMessage());
            }catch (Throwable t) {
                alarmSend(getType(), SEND_LEVEL_SMS, key);
                log.error(LOG_CONNECT_ERROR, getType(),  t.getMessage());
            }
        }
    }
}
