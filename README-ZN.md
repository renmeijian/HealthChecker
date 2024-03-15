# 简介

## HealthChecker可以做什么？

能检查集群中**所有服务**的**所有实例**健康状态，也能检查**MySql、redis、OSS、ETCD等依赖**的健康状态，方便扩展。


**背景** 

1、服务及组件运行过程中，不可避免的会出现不可用故障，除了保障高可用，还需对故障进行及时告警及恢复，避免引发全局不可用。

2、该健康检查组件只需在待检查服务添加主动健康检查组件客户端依赖，即可实现该服务的健康检查及告警功能。

3、对于数据库等组件，只需简单配置，即可实现中间件故障的秒级感知，而不是等客户反馈，提高故障处理速度及客户对产品的满意度。


主动健康检查及告警组件分为三部分，分别是**客户端、服务端、告警服务**。

本仓库中包含了客户端、服务端全部代码，不包括告警服务，由于不用业务的告警需求不同，兄弟们可以根据自己的业务需求开发自己的告警服务，将检查结果落库，然后制定相应的告警规则和告警途径。

## 检查类型

**被动健康检查**：SpringBoot微服务

**主动健康检查**：Mysql、redis、etcd、oss

## 总体流程

![9c9710ab90b02a56033b125831c94906](https://github.com/renmeijian/HealthChecker/assets/50255831/8b6653f9-87f1-448e-a9b9-42a857068d6f)



## 代码总体介绍

待补充.......

服务信息存储结构：

```java
    /**
     * namespace,  serviceName : set<instance>
     */
    public final Map<String, Map<String, Service>> serviceMap = new ConcurrentHashMap<>();
```




# 组件特点及实现

## 能力强

能检查集群中**所有服务**的**所有实例**健康状态，也能检查**MySql、redis、OSS、ETCD等依赖**的健康状态

## 易集成

本组件容易集成到springboot项目中，对代码无侵入。

### 对于微服务健康检查：只需两步

#### a、 在拟健康检查的项目中引入客户端依赖 

```xml
        <dependency>
            <groupId>rpa</groupId>
            <artifactId>health-client</artifactId>
            <version>0.0.1</version>
        </dependency>
```


#### b、在拟健康检查的项目中添加application.yml配置
配置举例：

```yml
# HealthChecker服务端部署地址
health-server:
  info:
    ip: 127.0.0.1
    port: 9001
    namespace: dev
```


### 对于MySql等中间件健康检查

#### a、在health-server开启对应中间件的健康检查功能，enableCheck为true表示开启该中间件的健康检查功能。然后配置中间件连接信息。

```yml
server:
  port: 9001
spring:
  application:
    name: health-server

health:
  mysql:
    enableCheck: false  # 是否开启mysql健康检查，true开启，false关闭
    ip: 172.30.92.11  # mysql访问地址
    port: 3306 # mysql访问端口
    database: rpa # mysql数据库名称
    user: root
    pwd: '*&T3*1(%imk@VB'
  redis:
    enableCheck: false # 是否开启redis健康检查，true开启，false关闭
    ip: 172.30.92.11
    port: 6379
    pwd: "&r6Fe$^7%NBm"
  s3:
    enableCheck: false # 是否开启s3健康检查，true开启，false关闭
    ip: "172.30.92.11:9000"
    bucketName: rpa
    prefix: resource/
    secretKey: "dyAwnn1eGIlxEepisVQAjHm6qzxXbF3x"
    accessKey: "grMSBA3SXispbLaB"
  etcd:
    enableCheck: false # 是否开启etcd健康检查，true开启，false关闭
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
