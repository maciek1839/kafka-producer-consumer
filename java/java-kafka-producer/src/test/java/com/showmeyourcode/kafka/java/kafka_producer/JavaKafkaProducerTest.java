package com.showmeyourcode.kafka.java.kafka_producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JavaKafkaProducerTest {

    @Test
    void shouldBuildValidAvroProducer(){
        JavaKafkaProducer producer = new JavaKafkaProducer.JavaKafkaProducerBuilder()
                .withNumberOfMessage(10L)
                .build();

        assertThat(producer.getNumberOfMessagesToProduce()).isEqualTo(10L);;
    }

    @Test
    void shouldProduceKafkaMessagesWhenConfigurationIsValid() throws KafkaProducerException {
        var kafkaProducer = Mockito.mock(Producer.class);
        when(kafkaProducer.send(any())).thenReturn(CompletableFuture.completedFuture(new RecordMetadata(new TopicPartition("topic1", 0), 1, 1, 1, Long.MIN_VALUE, 1, 1)));

        new JavaKafkaProducer(kafkaProducer, 2L).produce();

        verify(kafkaProducer, times(2)).send(any());
    }

    @Test
    void shouldThrowExceptionWhenCannotPublishMessages() {
        KafkaProducerException throwable = catchThrowableOfType(() -> {
            var producer = Mockito.mock(KafkaProducer.class);
            when(producer.send(any())).thenThrow(new RuntimeException("Buum!"));

            new JavaKafkaProducer(producer, 5L).produce();
        }, KafkaProducerException.class);

        assertThat(throwable).hasMessage("Cannot produce a record!  Kafka error: Buum!");
    }
}
