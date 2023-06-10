package com.showmeyourcode.kafka.java.starter;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JavaStarterIT {

    @Test
    void shouldRunEmbeddedKafka(){
        // todo: add an integration test with TestContainers or Embedded Kafka
        // https://github.com/confluentinc/kafka-streams-examples/blob/65f770c75988afc33d3813a1aa4f9f7818fe81d8/src/test/java/io/confluent/examples/streams/WikipediaFeedAvroExampleTest.java#L51
        assertThat(true).isTrue();
    }
}
