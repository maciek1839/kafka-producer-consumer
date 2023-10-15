package com.showmeyourcode.kafka.java_spring.service.messaging;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@SpringBootTest
@ActiveProfiles({"test-kafka", "streaming"})
@DirtiesContext
@EmbeddedKafka(topics = {
        "${app.kafka-streaming.input-topic}",
        "${app.kafka-streaming.output-topic}",
        "spring-streaming-app-counts-repartition"
})
class KafkaStreamingProcessorIT {

    @Test
    void shouldProduceMessagesToOutputTopic() {
        // TODO: verify messages are published
    }
}
