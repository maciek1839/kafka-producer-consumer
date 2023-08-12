package com.showmeyourcode.kafka.java.kafka_producer;

import com.showmeyourcode.kafka.java.common.KafkaProperties;
import com.showmeyourcode.kafka.java.kafka_producer.avro.ExampleUserRecord;
import com.showmeyourcode.kafka.java.kafka_producer.avro.ExampleUserRecord2;
import org.apache.avro.reflect.ReflectData;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JavaJavaKafkaAvroProducerClassTest {

    @Test
    void shouldBuildValidAvroProducer(){
        JavaKafkaAvroProducer avroClassProducer = new JavaKafkaAvroProducer.JavaKafkaAvroProducerBuilder()
                .withName("Avro Class Test")
                .withAvroClass()
                .withNumberOfMessage(10L)
                .build();
        JavaKafkaAvroProducer avroFileProducer = new JavaKafkaAvroProducer.JavaKafkaAvroProducerBuilder()
                .withName("Avro File Test")
                .withAvroClassFromFile()
                .withNumberOfMessage(50L)
                .build();

        assertThat(avroClassProducer.getProducerName()).isEqualTo("Avro Class Test");
        assertThat(avroClassProducer.getNumberOfMessagesToProduce()).isEqualTo(10L);
        assertThat(avroClassProducer.getGetSchema().apply(null).getName()).isEqualTo("ExampleUserRecord");

        assertThat(avroFileProducer.getProducerName()).isEqualTo("Avro File Test");
        assertThat(avroFileProducer.getNumberOfMessagesToProduce()).isEqualTo(50L);
        assertThat(avroFileProducer.getGetSchema().apply(null).getName()).isEqualTo("ExampleUserRecord2");
    }

    @Test
    void shouldProduceKafkaMessagesWhenAvroClassConfigurationIsValid()  {
        var kafkaProducer = Mockito.mock(Producer.class);
        when(kafkaProducer.send(any(), any())).thenReturn(CompletableFuture.completedFuture(new RecordMetadata(new TopicPartition("topic1", 0), 1, 1, 1, Long.MIN_VALUE, 1, 1)));

        new JavaKafkaAvroProducer(
                kafkaProducer,
                2L,
                "Avro Class",
                unused -> ReflectData.get().getSchema(ExampleUserRecord.class)
        ).produce();

        ArgumentCaptor<ProducerRecord> argument = ArgumentCaptor.forClass(ProducerRecord.class);
        verify(kafkaProducer, times(2)).send(argument.capture(), any());
        argument.getAllValues().forEach( record ->{
            assertThat(record.topic()).isEqualTo(KafkaProperties.TOPIC);
            // TODO: deserialize to POJO
        });
    }

    @Test
    void shouldProduceKafkaMessagesWhenAvroFileConfigurationIsValid() {
        var kafkaProducer = Mockito.mock(Producer.class);
        when(kafkaProducer.send(any(), any())).thenReturn(CompletableFuture.completedFuture(new RecordMetadata(new TopicPartition("topic1", 0), 1, 1, 1, Long.MIN_VALUE, 1, 1)));

        new JavaKafkaAvroProducer(kafkaProducer, 2L, "Avro File", unused -> ReflectData.get().getSchema(ExampleUserRecord2.class)).produce();

        verify(kafkaProducer, times(2)).send(any(), any());
    }

    @Test
    void shouldThrowExceptionWhenCannotPublishMessages() {
        catchThrowableOfType(() -> {
            var producer = Mockito.mock(KafkaProducer.class);
            when(producer.send(any(), any())).thenThrow(new RuntimeException("Buum!"));

            new JavaKafkaAvroProducer(producer, 5L, "test producer", unused -> ReflectData.get().getSchema(ExampleUserRecord.class)).produce();
        }, RuntimeException.class);
    }
}
