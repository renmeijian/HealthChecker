package com.iflytek.rpa.registryServer.net;

import com.iflytek.rpa.registryServer.core.entity.SendDo;
import com.iflytek.rpa.registryServer.core.healthcheck.config.AlarmConfig;
import com.iflytek.rpa.registryServer.core.healthcheck.processor.MysqlHealthCheckProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

import static com.iflytek.rpa.registryServer.core.constant.Constances.*;

/**
 * @author mjren
 * @date 2023-11-28 14:32
 * @copyright Copyright (c) 2023 mjren
 */
public class NetUtils {
    private final static Logger log = LoggerFactory.getLogger(NetUtils.class);

    private static RestTemplate restTemplate;

    private static AlarmConfig alarmConfig;

    public  static void alarmSend(String type, String sendType,String alarmInfo){
        try {
            SendDo sendDo = new SendDo();
            sendDo.setType(type);
            sendDo.setSendType(sendType);
            sendDo.setAlarmInfo(alarmInfo);
            String url = HTTP + alarmConfig.getIp() + COLON + alarmConfig.getPort() + SEND_URI;
            restTemplate.postForEntity(url, sendDo, String.class);
        }catch (Exception e){
            log.error("{} alarmSend error:{}", alarmInfo, e.getMessage());
        }
    }
}
