package com.iflytek.rpa.registryServer.core.constant;

import java.util.Stack;

/**
 * @author mjren
 * @date 2023-11-27 14:45
 * @copyright Copyright (c) 2023 mjren
 */
public class Constances {

    public static final String COLON = ":";

    public static final String SPLIT = "/";

    public static final int ZERO_SECOND = 0;

    public static final int TWO_SECOND = 2000;

    public static final int FIVE_SECOND = 5000;

    public static final Integer TIMEOUT_UNHEALTHY = 15;

    public static final Integer TIMEOUT_DELETE = 30;
    public static final String LOG_UNHEALTHY = "[Heartbeat detection] marking instances as unhealthy, instance information:{}";

    public static final String LOG_DELETE = "[AUTO-DELETE-IP] service: {}, ip: {}";

    public static final String LOG_BEAT_EXCEPTION = "Exception while processing client beat time out.";

    public static final String LOG_HEALTHY_ERROR = "[HEALTH-CHECK] error while process health check for {}:{}";

    public static final String LOG_NO_PROCESSOR = "Not loaded into processor!";

    public static final String LOG_CHECK_FAIL = "[{}] health check fail，reason：{}";

    public static final String LOG_HEALTHY = "[{}] is healthy，response：{}";

    public static final String LOG_CONNECT_ERROR = "[{}] connection error, probably not reachable: {}";

    public static final String LOG_EXECUTE_ERROR = "[{}] execute error: {}";

    public static final String LOG_CLOSE_STATEMENT = "[{}] failed to close statement:{}";

    public static final String LOG_CLOSE_RESULTSET = "[{}] failed to close resultSet:{}";

    public static final String MYSQL = "mysql";

    public static final String REDIS = "redis";

    public static final String S3 = "s3";

    public static final String ETCD = "etcd";

    public static final String REDIS_KEY = "k1";

    public static final String ETCD_KEY = "/apisix/consumers";

    public static final String  HTTP = "http://";

    public static final String MYSQL_JDBC = "jdbc:mysql://";

    public static final String MYSQL_OTHER = "?serverTimezone=GMT%2B8&zeroDateTimeBehavior=round&useUnicode=true&characterEncoding=utf-8&useSSL=false";

    public static final String MYSQL_CONNECT_TIMEOUT = "&connectTimeout=";

    public static final String MYSQL_SOCKET_TIMEOUT = "&socketTimeout=";

    public static final String MYSQL_LOGIN_TIMEOUT = "&loginTimeout=1";

    public static final int MYSQL_QUERY_TIMEOUT = 1;

    public static final int MYSQL_INDEX = 2;

    public static final int MYSQL_CONNECT_TIMEOUT_MS = 500;

    public static final String CHECK_MYSQL_MASTER_SQL = "show global variables where variable_name='read_only'";

    public static final String SEND_LEVEL_EMAIL = "email";

    public static final String SEND_LEVEL_SMS = "sms";

    public static final String SEND_URI = "/rpa/send";

    public static final String LOG_INVALID = "INVALID_PARAM";

    public static final String LOG_NOTFOUND = "service not found, info: ";

    public static final String LOG_IP_EMPTY = "ip list can not be empty, service: ";

    public static final String LOG_IP = ", ip list: ";

    public static final String LOG_CREATE_EMPTY = "creating empty service {}:{}";

    public static final String LOG_NEW_SERVICE = "[NEW-SERVICE] {}";

    public static final String LOG_NS_NULL = "nameSpace: {}，service: {}, Data is null";

    public static final String LOG_CHANGE_ERROR = "error while changing instance:";

    public static final String LOG_CHANGE = "[registry] data is changed, serviceName: {}";

    public static final String LOG_BEAT_NS_NULL = "nameSpace is null";

    public static final String LOG_INSTANCE_NULL = "[CLIENT-BEAT] The instance has been removed for health mechanism, perform data compensation operations, beat: {}, serviceName: {}";

    public static final String RESOURCE_NOT_FOUND = "RESOURCE_NOT_FOUND";

    public static final String LOG_SERVER_ERROR = "SERVER_ERROR";

    public static final String LOG_SERVICE_NULL = "service not found: ";

    public static final String LOG_PARAM_ILLEGAL = "Param 'serviceName' is illegal, can't be empty";

    public static final String LOG_ERROR_HANDLE = "Error while serviceManager handle!";

    public static final String THREAD_CONSISTENCY = "com.iflytek.rpa.registryServer.consistency.notifier";

    public static final String THREAD_HEALTHY =  "com.iflytek.rpa.registryServer.core.health";

    public static final String THREAD_CHECKER = "com.iflytek.rpa.registryServer.core.persistent.checker";

}
