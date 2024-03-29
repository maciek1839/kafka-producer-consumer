package com.showmeyourcode.kafka.java.kafka_consumer;

import com.showmeyourcode.kafka.java.common.KafkaProperties;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.InvalidGroupIdException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JavaKafkaConsumerTest {

    @Test
    void shouldBuildValidConsumer(){
        JavaKafkaConsumer kafkaConsumer = new JavaKafkaConsumer.JavaKafkaConsumerBuilder()
                .withNumberOfMessages(10L)
                .build();

        assertThat(kafkaConsumer.getNumberOfMessagesToConsume()).isEqualTo(10L);
    }

    @Test
    void shouldConsumeKafkaMessagesWhenConfigurationIsValid()  {
        var kafkaConsumer = Mockito.mock(Consumer.class);
        when(kafkaConsumer.poll(any())).thenReturn(new ConsumerRecords(Map.of(new TopicPartition("topic1", 0), List.of(new ConsumerRecord<>("topic", 0, 123L, "key", "value")))));

        new JavaKafkaConsumer(kafkaConsumer, 2L).consume();

        verify(kafkaConsumer, times(2)).poll(any());
    }

    @Test
    void shouldThrowExceptionWhenCannotFetchMessages() {
        InvalidGroupIdException throwable = catchThrowableOfType(() -> {
            final var props = new Properties();
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProperties.BOOTSTRAP_SERVERS);
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

            new JavaKafkaConsumer(new KafkaConsumer<>(props), 5L).consume();
        }, InvalidGroupIdException.class);

        assertThat(throwable)
                .hasMessage("To use the group management or offset commit APIs, you must provide a valid group.id in the consumer configuration.");
    }
}
