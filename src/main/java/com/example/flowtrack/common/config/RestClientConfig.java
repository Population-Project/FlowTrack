package com.example.flowtrack.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient() {

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000);  // 서버와 연결: 3초
        factory.setReadTimeout(5000);     // 응답 기다림: 5초

        return RestClient.builder()
                .requestFactory(factory)
                .build();
    }
}
