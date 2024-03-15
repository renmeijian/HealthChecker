# Introduction

## What can HealthChecker do？

It can check the health status of all instances of all services in the cluster, as well as the health status of dependencies such as MySql, Redis, OSS, ETCD, etc., for easy scalability.


**Background** 

1. During the operation of services and components, unavailability failures are inevitable. In addition to ensuring high availability, timely warning and recovery of failures are also necessary to avoid causing global unavailability.

2. The health check component only needs to add an active health check component client dependency to the service to be checked to achieve the health check and alarm functions of the service.

3. For components such as databases, simple configuration is required to achieve second level perception of middleware failures, rather than waiting for customer feedback, improving fault handling speed and customer satisfaction with the product.

The active health check and alarm component is divided into three parts, namely ** client, server, and alarm service **.

This repository contains all the code for the client and server, excluding alarm services. Due to the different alarm requirements of different businesses, you can develop your own alarm services according to your own business needs, store the inspection results, and then formulate corresponding alarm rules and channels.

## Type of Check

**Passive health check**：SpringBoot

**Proactive health check**：Mysql、redis、etcd、oss

## Overall process

![9c9710ab90b02a56033b125831c94906](https://github.com/renmeijian/HealthChecker/assets/50255831/8b6653f9-87f1-448e-a9b9-42a857068d6f)



## Overall Code Introduction

To be supplemented.......

Service Information Storage Structure：

```java
    /**
     * namespace,  serviceName : set<instance>
     */
    public final Map<String, Map<String, Service>> serviceMap = new ConcurrentHashMap<>();
```




# Component characteristics and implementation

## Be capable

Can check the health status of all instances of all services in the cluster, as well as the health status of dependencies such as MySql, Redis, OSS, ETCD, etc

## Easy to integrate

This component is easy to integrate into the Spring Boot project with minimal code intrusion.

### For microservice health check: only two steps needed

#### a、 Introducing client dependencies in the project of health check planning

```xml
        <dependency>
            <groupId>rpa</groupId>
            <artifactId>health-client</artifactId>
            <version>0.0.1</version>
        </dependency>
```


#### b、Add application.yml configuration to the project for health check

Configuration example:

```yml
# HealthChecker Server deployment address
health-server:
  info:
    ip: 127.0.0.1
    port: 9001
    namespace: dev
```


### For middleware health checks such as MySql

#### a、Enable the health check function of the corresponding middleware in the health server, where enableCheck is true to indicate that the health check function of the middleware is enabled. Then configure the middleware connection information.

```yml
server:
  port: 9001
spring:
  application:
    name: health-server

health:
  mysql:
    enableCheck: false  # Is MySQL health check enabled? True enabled, false disabled
    ip: 172.30.92.11  # MySQL access address
    port: 3306 # MySQL access port
    database: rpa # MySQL database name
    user: root
    pwd: '*&T3*1(%imk@VB'
  redis:
    enableCheck: false # Is Redis health check enabled? True enabled, false disabled
    ip: 172.30.92.11
    port: 6379
    pwd: "&r6Fe$^7%NBm"
  s3:
    enableCheck: false # Is S3 health check enabled? True enabled, false disabled
    ip: "172.30.92.11:9000"
    bucketName: rpa
    prefix: resource/
    secretKey: "dyAwnn1eGIlxEepisVQAjHm6qzxXbF3x"
    accessKey: "grMSBA3SXispbLaB"
  etcd:
    enableCheck: false # Is the ETCD health check enabled? True enabled, false disabled
    ip: 172.30.92.11
    port: 22379
```


#### b、若有其他中间件需要健康检查，本组件支持扩展，扩展步骤如下：

##### 创建配置映射类

如redis配置：添加必须的注解，设置type值（如redis）, 添加连接信息字段，如下图：
    
![image](https://github.com/renmeijian/HealthChecker/assets/50255831/457799e6-65d4-4a66-9e3a-1f86a2aadd3b)

##### 编写对应的健康检查处理器

添加@HealthChecker注解，设置type值。实现HealthCheckProcessor接口，开启一个线程进行健康检查，如下图：
    
![image](https://github.com/renmeijian/HealthChecker/assets/50255831/d3e5d91d-b49a-446f-857b-21f563a1ea8d)

    

## 高性能

**异步化**

阻塞队列

将服务注册的任务放入阻塞队列，采用线程池异步来完成实例更新，从而提高并发写能力。

**线程池**

定时线程可复用

**连接池**

数据库检查连接可复用

 **双重检查锁**  
 
 确保在应用程序中只有一个实例

## 低耦合

**观察者模式**

尽量减少依赖关系，使之便于维护 ，耦合度低

## 功能内聚

注册处理，心跳处理，发送http请求等功能单独封装类

## 扩展性强

**策略模式**

根据任务的类型选择相应的 HealthCheckProcessor 实例进行处理，将具体的处理逻辑委托给对应的处理器


# 其他技术点

**同步锁**

对修改服务列表的动作加锁处理，避免并发修改的安全问题


**双重检查锁**

保证线程安全，减少同步开销

**copyOnWrite技术**

在addIPAddress方法中，会拷贝旧的实例列表，添加新实例到列表中。完成对实例状态更新后，则会用新列表直接覆盖旧实例列表。而在更新过程中，旧实例列表不受影响，依然可以在处理心跳，判断健康状态时进行读取。



# 下一步计划

health-server高可用，实现多实例部署，实现节点间数据同步
