package com.example.botapp.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class BackendConfiguration {
    final ApplicationConfig applicationConfig;

    @Autowired
    public BackendConfiguration(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    @Bean
    public WebClient backendWebClient() {
        return WebClient.builder()
                .baseUrl(applicationConfig.backendUrl())
                .defaultHeader("Message", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

}
