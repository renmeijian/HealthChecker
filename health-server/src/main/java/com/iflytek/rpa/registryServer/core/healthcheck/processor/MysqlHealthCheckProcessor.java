package com.iflytek.rpa.registryServer.core.healthcheck.processor;

import com.iflytek.rpa.registryServer.executor.GlobalExecutor;
import com.iflytek.rpa.registryServer.conf.HealthChecker;
import com.iflytek.rpa.registryServer.core.entity.Instance;
import com.iflytek.rpa.registryServer.core.healthcheck.HealthCheckTask;
import com.iflytek.rpa.registryServer.core.healthcheck.config.HealthMysqlConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.sql.*;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.iflytek.rpa.registryServer.core.constant.Constances.*;
import static com.iflytek.rpa.registryServer.net.NetUtils.alarmSend;

@HealthChecker(type = MYSQL)
@Component
public class MysqlHealthCheckProcessor implements HealthCheckProcessor {
    private final static Logger log = LoggerFactory.getLogger(MysqlHealthCheckProcessor.class);

    private static final ConcurrentMap<String, Connection> CONNECTION_POOL = new ConcurrentHashMap<String, Connection>();

    @Override
    public String getType() {
        return MYSQL;
    }

    @Override
    public void process(HealthCheckTask task) {
        Set<Instance> configs = task.getService().getInstanceSet();
        if (CollectionUtils.isEmpty(configs)) {
            return;
        }
        for (Instance config : configs) {
            try {
                GlobalExecutor.executeCheckTask(new MysqlCheckTask(config));
            } catch (Exception e) {
               log.error(LOG_CHECK_FAIL, getType(), e.getMessage());
            }
        }
    }

    private class MysqlCheckTask implements Runnable {
        private final HealthMysqlConfig mysqlConfig;

        public MysqlCheckTask(Instance config) {
            this.mysqlConfig = (HealthMysqlConfig) config;
        }

        @Override
        public void run() {
            Statement statement = null;
            ResultSet resultSet = null;
            String key = getType() + COLON + mysqlConfig.getIp() + COLON + mysqlConfig.getPort();
            try {
                Connection connection = CONNECTION_POOL.get(key);

                if (connection == null || connection.isClosed()) {
                    String url = MYSQL_JDBC + mysqlConfig.getIp() + COLON + mysqlConfig.getPort() + SPLIT + mysqlConfig.getDatabase()
                            + MYSQL_OTHER + MYSQL_CONNECT_TIMEOUT + MYSQL_CONNECT_TIMEOUT_MS + MYSQL_SOCKET_TIMEOUT
                            + MYSQL_CONNECT_TIMEOUT_MS + MYSQL_LOGIN_TIMEOUT;

                    connection = DriverManager.getConnection(url, mysqlConfig.getUser(), mysqlConfig.getPwd());
                    CONNECTION_POOL.put(key, connection);
                }
                statement = connection.createStatement();
                statement.setQueryTimeout(MYSQL_QUERY_TIMEOUT);
                resultSet = statement.executeQuery(CHECK_MYSQL_MASTER_SQL);
                resultSet.next();
                String result = resultSet.getString(MYSQL_INDEX);
                log.debug(LOG_HEALTHY,getType(),  result);
            } catch (SQLException e) {
                alarmSend(getType(), SEND_LEVEL_SMS, key);
                log.error(LOG_EXECUTE_ERROR, getType(), e.getMessage());
            }catch (Throwable t) {
                alarmSend(getType(), SEND_LEVEL_SMS, key);
                log.error(LOG_CONNECT_ERROR, getType(),  t.getMessage());
            } finally {
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        log.error(LOG_CLOSE_STATEMENT, getType(), statement, e);
                    }
                }
                if (resultSet != null) {
                    try {
                        resultSet.close();
                    } catch (SQLException e) {
                        log.error(LOG_CLOSE_RESULTSET, getType(), resultSet, e);
                    }
                }
            }
        }
    }
}
