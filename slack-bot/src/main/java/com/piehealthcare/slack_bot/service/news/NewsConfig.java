package com.piehealthcare.slack_bot.service.news;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class NewsConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
