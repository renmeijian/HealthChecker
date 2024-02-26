package com.iflytek.rpa.registry.config;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 将我们需要的类注入IOC容器
 */
public class InitRegistrySelect implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {


        return new String[]{
                // 服务注册的事件监听
                "com.iflytek.rpa.registry.core.ClientRegistry",
                // 服务注册的事件发布
                "com.iflytek.rpa.registry.config.ClientSmartLifecycle",
                // 客户端信息
                "com.iflytek.rpa.registry.core.entity.ServerInfo",
                "com.iflytek.rpa.registry.net.NamingProxy"
        };
    }
}