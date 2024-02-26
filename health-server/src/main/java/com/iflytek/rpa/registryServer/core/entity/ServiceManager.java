package com.iflytek.rpa.registryServer.core.entity;

import com.alibaba.fastjson.JSON;
import com.iflytek.rpa.registryServer.executor.HealthCheckReactor;
import com.iflytek.rpa.registryServer.conf.RegistryException;
import com.iflytek.rpa.registryServer.consistency.ConsistencyService;
import com.iflytek.rpa.registryServer.core.healthcheck.HealthCheckTask;
import com.iflytek.rpa.registryServer.core.healthcheck.event.HealthEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

import static com.iflytek.rpa.registryServer.core.constant.Constances.*;


@Component
public class ServiceManager {
    private final static Logger log = LoggerFactory.getLogger(ServiceManager.class);

    /**
     * namespace,  serviceName : set<instance>
     */
    public final Map<String, Map<String, Service>> serviceMap = new ConcurrentHashMap<>();

    private final Object putServiceLock = new Object();

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private ConsistencyService consistencyService;

    public void registerInstance(String nameSpace, String serviceName,Instance instance) throws RegistryException {
        createEmptyService(nameSpace, serviceName);
        Service service = getService(nameSpace, serviceName);
        if (service == null) {
            throw new RegistryException(LOG_INVALID, LOG_NOTFOUND + nameSpace + serviceName);
        }
        addInstance(nameSpace, serviceName, instance);
    }

    public void addInstance(String nameSpace, String serviceName,  Instance... ips) {
        Service service = getService(nameSpace, serviceName);
        synchronized (service) {
            List<Instance> instanceList = addIpAddresses(service, ips);
            Instances instances = new Instances();
            instances.setNameSpace(nameSpace);
            instances.setServiceName(serviceName);
            instances.setInstanceList(instanceList);
            consistencyService.put(instances);
        }
    }
    private List<Instance> addIpAddresses(Service service, Instance... ips)  {
        return updateIpAddresses(service, ips);
    }

    /**
     * 先获取旧的实例列表，然后把新的实例信息与旧的做对比
     * 新的实例就添加，老的实例就更新健康状态，然后返回最新的实例列表
     */
    public List<Instance> updateIpAddresses(Service service, Instance... ips) {
        Set<Instance> oldInstanceSet = service.getInstanceSet();
        List<Instance> oldInstanceList = null;
        if(null != oldInstanceSet){
            oldInstanceList = new ArrayList<>(oldInstanceSet);
        }
        Map<String, Instance> instanceMap;
        if (!CollectionUtils.isEmpty(oldInstanceList)) {
            instanceMap = setValid(oldInstanceList);
        } else {
            instanceMap = new HashMap<>(ips.length);
        }
        for (Instance instance : ips) {
            instanceMap.put(instance.toIpAddr(), instance);
        }
        if (CollectionUtils.isEmpty(instanceMap)) {
            throw new IllegalArgumentException(
                    LOG_IP_EMPTY + service.getServiceName() + LOG_IP + JSON.toJSONString(instanceMap.values()));
        }
        return new ArrayList<>(instanceMap.values());
    }

    private Map<String, Instance> setValid(List<Instance> oldInstances) {

        Map<String, Instance> instanceMap = new HashMap<>(oldInstances.size());
        for (Instance instance : oldInstances) {
            if (instance != null) {
                instance.setHealthy(true);
                instance.setLastBeat(System.currentTimeMillis());
                instanceMap.put(instance.toIpAddr(), instance);
            }
        }
        return instanceMap;
    }

    public void createEmptyService(String namespace, String serviceName){
        Service service = getService(namespace, serviceName);
        if (service == null) {
            log.info(LOG_CREATE_EMPTY, namespace, serviceName);
            service = new Service();
            service.setServiceName(serviceName);
            service.setNameSpace(namespace);
            putServiceAndInit(service);
        }
    }

    private void putServiceAndInit(Service service){
        putService(service);
        service = getService(service.getNameSpace(), service.getServiceName());
        service.init();
        log.info(LOG_NEW_SERVICE, JSON.toJSONString(service));
    }

    public void putService(Service service) {
        if (!serviceMap.containsKey(service.getNameSpace())) {
            synchronized (putServiceLock) {
                if (!serviceMap.containsKey(service.getNameSpace())) {
                    //ConcurrentSkipListMap提供比较高效的并发读取和修改操作
                    serviceMap.put(service.getNameSpace(), new ConcurrentSkipListMap<>());
                }
            }
        }
        Map<String, Service> map = serviceMap.get(service.getNameSpace());
        map.putIfAbsent(service.getServiceName(), service);
    }

    public Service getService(String namespaceId, String serviceName) {
        if (serviceMap.get(namespaceId) == null) {
            return null;
        }
        return serviceMap.get(namespaceId).get(serviceName);
    }

    public void handle(Instances instances) {
        try {
            String nameSpace = instances.getNameSpace();
            String serviceName = instances.getServiceName();
            if(null == serviceMap.get(nameSpace) || null == serviceMap.get(nameSpace).get(serviceName)){
                log.error(LOG_NS_NULL, nameSpace, serviceName);
            }
            serviceMap.get(nameSpace).get(serviceName).onChange(instances);

        } catch (Throwable e) {
            log.error(LOG_CHANGE_ERROR, e);
        }
    }

    public Instance getInstance(String namespaceId, String serviceName, String ip, String port) {
        Service service = getService(namespaceId, serviceName);
        if (service == null) {
            return null;
        }
        List<Instance> instanceList = new ArrayList<>(service.getInstanceSet());
        if (CollectionUtils.isEmpty(instanceList)) {
            return null;
        }
        for (Instance instance : instanceList) {
            if (instance.getIp().equals(ip) && instance.getPort().equals(port)) {
                return instance;
            }
        }
        return null;
    }

    @EventListener
    public void healthCheckForPersistent(HealthEvent healthEvent) {
        Map<String, Service> serviceMap = new ConcurrentHashMap<>();
        for (Instance instance : healthEvent.getConfigList()) {
            Service service = new Service();
            Set<Instance> instanceSet = new HashSet<>();
            instanceSet.add(instance);
            service.setInstanceSet(instanceSet);
            service.setType(instance.getType());
            serviceMap.put(instance.getType(), service);
        }
        for (Map.Entry<String, Service> entry : serviceMap.entrySet()) {
            HealthCheckTask checkTask = new HealthCheckTask(entry.getValue(), applicationContext);
            HealthCheckReactor.scheduleCheck(checkTask);
        }
    }

}
