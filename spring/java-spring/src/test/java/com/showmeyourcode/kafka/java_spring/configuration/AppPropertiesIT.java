package com.showmeyourcode.kafka.java_spring.configuration;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("kafka-disabled")
class AppPropertiesIT {

    @Autowired
    private AppProperties appProperties;

    @Test
    void shouldLoadConfigurationProperties() {
        assertThat(appProperties.isConsumerEnabled()).isFalse();
        assertThat(appProperties.isProducerEnabled()).isFalse();
        assertThat(appProperties.getKafka()).isNotNull();
        assertThat(appProperties.getKafka().getTopicName()).isEqualTo("spring.kafka.topic");
    }
}
