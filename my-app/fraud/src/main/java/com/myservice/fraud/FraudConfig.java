package com.myservice.fraud;

import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
public class FraudConfig {
    @Bean
    @LoadBalanced
    public HttpClient httpClient(){
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();
    }
}

