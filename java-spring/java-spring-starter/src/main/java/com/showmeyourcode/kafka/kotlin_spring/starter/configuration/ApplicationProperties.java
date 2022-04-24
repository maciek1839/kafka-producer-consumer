package com.showmeyourcode.kafka.kotlin_spring.starter.configuration;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

import javax.annotation.PostConstruct;

@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

    private boolean isProducerEnabled;
    private boolean isConsumerEnabled;

    @PostConstruct
    private void setup(){
        log.info("Configuration: {}", this);
    }
}
