package com.example.alipay.config;

import com.example.alipay.bean.AlipayConfigInfo;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Blake on 2018/11/1
 */
@Configuration
public class AliPayConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "alipay.cfg")
    public AlipayConfigInfo alipayConfigInfo() {

        return new AlipayConfigInfo();
    }

}
