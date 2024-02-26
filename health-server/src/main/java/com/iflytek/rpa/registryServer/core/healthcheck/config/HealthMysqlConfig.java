package com.iflytek.rpa.registryServer.core.healthcheck.config;

import com.iflytek.rpa.registryServer.conf.ConfigInit;
import com.iflytek.rpa.registryServer.core.entity.Instance;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "health.mysql")
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@ConfigInit
public class HealthMysqlConfig extends Instance {
    private String type = "mysql";

    private String nameSpace;

    private String database;

    private String user;

    private String pwd;

    private String cmd;

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
}
