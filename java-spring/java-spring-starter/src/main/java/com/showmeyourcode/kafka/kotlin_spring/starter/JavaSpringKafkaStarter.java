package com.showmeyourcode.kafka.kotlin_spring.starter;

import com.showmeyourcode.kafka.kotlin_spring.starter.service.ApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Slf4j
@SpringBootApplication
@EnableConfigurationProperties
public class JavaSpringKafkaStarter {

    public static void main(String[] args) {
        log.info("Starting the instance {}...", ApplicationService.getInstanceID());
        SpringApplication.run(JavaSpringKafkaStarter.class, args);
        log.info("The instance {} started.", ApplicationService.getInstanceID());
    }
}
