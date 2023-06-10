package com.showmeyourcode.kafka.java_spring.configuration;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Slf4j
@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private boolean isProducerEnabled;
    private boolean isConsumerEnabled;
    private AppKafkaProperties kafka;

    @PostConstruct
    private void setup(){
        log.info("Configuration: {}", this);
    }

    @Data
    public static class AppKafkaProperties {
        private String topicName;
        private String schedulerFixedRate;
    }
}
