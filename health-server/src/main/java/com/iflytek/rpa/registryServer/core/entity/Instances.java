package com.iflytek.rpa.registryServer.core.entity;

import java.util.ArrayList;
import java.util.List;

public class Instances {

    private List<Instance> instanceList = new ArrayList<>();

    private String nameSpace;

    private String serviceName;


    public List<Instance> getInstanceList() {
        return instanceList;
    }

    public void setInstanceList(List<Instance> instanceList) {
        this.instanceList = instanceList;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
