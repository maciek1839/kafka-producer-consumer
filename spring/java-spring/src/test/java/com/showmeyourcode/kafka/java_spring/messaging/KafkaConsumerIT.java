package com.showmeyourcode.kafka.java_spring.messaging;

import com.showmeyourcode.kafka.java_spring.configuration.AppProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowableOfType;

@SpringBootTest
@ActiveProfiles("consumer")
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class KafkaConsumerIT {

    @Autowired
    private ApplicationContext context;
    @Autowired
    private AppProperties properties;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    void shouldConsumeKafkaMessageWhenConfigurationIsValid() throws ExecutionException, InterruptedException {
        assertThat(context.getBean("kafkaConsumer")).isNotNull();
        catchThrowableOfType(() -> context.getBean("kafkaProducer"), NoSuchBeanDefinitionException.class);

        // todo: make an asynchronous test
        SendResult<String, String> sendResult = kafkaTemplate.send(properties.getKafka().getTopicName(), "").get();
        assertThat(sendResult.getProducerRecord().topic()).isEqualTo(properties.getKafka().getTopicName());
        assertThat(sendResult.getRecordMetadata().topic()).isEqualTo(properties.getKafka().getTopicName());

        // wait for distributing a message to consumers
        Thread.sleep(5000);

        // todo: assertion
    }
}
