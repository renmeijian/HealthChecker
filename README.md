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


#### b、If there are other middleware that require health checks, this component supports extension. The extension steps are as follows：

##### Create configuration mapping class

For Redis configuration: add necessary annotations, set type values (such as Redis), and add connection information fields, as shown in the following figure:
    
![image](https://github.com/renmeijian/HealthChecker/assets/50255831/457799e6-65d4-4a66-9e3a-1f86a2aadd3b)

##### Write the corresponding health check processor

Add @ HealthChecker annotation and set the type value. Implement the HealthCheckProcessor interface and start a thread for health checks, as shown in the following figure:
    
![image](https://github.com/renmeijian/HealthChecker/assets/50255831/d3e5d91d-b49a-446f-857b-21f563a1ea8d)

    

## High-performance

**Asynchronous**

Blocking queue

Put the task of service registration into a blocking queue and use thread pool stepping to complete instance updates, thereby improving concurrent writing capabilities.

**Thread pool**

 Timed threads can be reused

**Connection pool**

 Database check connection reusability

**Double-Checked Lock**  
 
 Ensure that there is only one instance in the application

## Low Coupling

**Observer mode**

Minimize dependencies as much as possible, making them easier to maintain and with low coupling

## Functional Cohesion

Registration processing, heartbeat processing, sending HTTP requests, and other functions are separately packaged into classes

## Strong scalability

**Strategy mode**

Select the corresponding HealthCheckProcessor instance based on the type of task for processing, and delegate the specific processing logic to the corresponding processor


# Other technical points

**synchronized**

Lock the action of modifying the service list to avoid security issues of concurrent modifications


**Double-Checked Lock**

Ensure thread safety and reduce synchronization overhead

**CopyOnWrite**

In the addIPAddress method, the old instance list will be copied and a new instance will be added to the list. After updating the instance status, the old instance list will be directly overwritten with the new list. During the update process, the old instance list is not affected and can still be read while processing heartbeats and determining health status.



# The next step

Health server is highly available, enabling multi instance deployment and data synchronization between nodes
