package com.iflytek.rpa.registry.config;

/**
 * @author mjren
 * @date 2023-12-07 14:28
 * @copyright Copyright (c) 2023 mjren
 */
public class constants {
    public static final String COLON = ":";

    public static final String SPLIT = "/";

    public static final String  HTTP = "http://";

    public static final String  IP = "ip";

    public static final String  PORT = "port";

    public static final String  SERVICE_NAME = "serviceName";

    public static final String  NAME_SPACE = "nameSpace";

    public static final String  BEAT_URI = "/rpa/registry/beat";

    public static final String  REGISTRY_URI = "/rpa/registry/setRegisterContext";

    public static final String LOG_REGISTRY_ERROR = "registry error:{}";

    public static final String THREAD_SENDER = "com.iflytek.rpa.registry.beat.sender";

    public static final String RESOURCE_NOT_FOUND = "RESOURCE_NOT_FOUND";

    public static final String LOG_BEAT_REGIST_ERROR = "Beat registry error:{}";

    public static final String LOG_BEAT_SEND_ERROR = "[CLIENT-BEAT] failed to send beat: {}, exception: {}";

    public static final String LOG_SERVER_PORT =  "server.port";

    public static final String LOG_APPLICATION_NAME = "spring.application.name";

}
