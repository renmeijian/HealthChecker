package com.iflytek.rpa.registry.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

/**
 * @Import一个类 该类实现了ImportSelector 重写selectImports方法该方法返回了String[]数组的对象，数组里面的类都会注入到spring容器当中
 */

@Configuration(proxyBeanMethods = false)
@Import({InitRegistrySelect.class})
public class EnableRegistryConfiguration {

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}