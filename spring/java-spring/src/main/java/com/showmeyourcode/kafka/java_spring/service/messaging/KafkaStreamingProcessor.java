package com.showmeyourcode.kafka.java_spring.service.messaging;

import com.github.javafaker.Faker;
import com.showmeyourcode.kafka.java_spring.configuration.AppProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

/**
 * https://www.baeldung.com/spring-boot-kafka-streams
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "is-streaming-enabled", havingValue = "true")
public class KafkaStreamingProcessor {

    private static final Serde<String> STRING_SERDE = Serdes.String();
    private static final Faker faker = new Faker();

    private final StreamsBuilder streamsBuilder;
    private final AppProperties appProperties;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @PostConstruct
    void postConstruct() {
        log.info("Started processing stream messages...");
        KStream<String, String> messageStream = streamsBuilder
                .stream(appProperties.getKafkaStreaming().getInputTopic(), Consumed.with(STRING_SERDE, STRING_SERDE));

        KTable<String, Long> wordCounts = messageStream
                .mapValues((ValueMapper<String, String>) String::toLowerCase)
                .flatMapValues(value -> Arrays.asList(value.split("\\W+")))
                .groupBy((key, word) -> word, Grouped.with(STRING_SERDE, STRING_SERDE))
                .count(Materialized.as("counts"));

        wordCounts.toStream().to(appProperties.getKafkaStreaming().getOutputTopic());
    }

    @Scheduled(fixedRateString ="${app.kafka-streaming.scheduler-fixed-rate}")
    void publishStreamMessages(){
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();

        String finalRecord = String.format("Name: %s %s ", firstName, lastName);
        log.info("Publishing a new stream message - {}", finalRecord);

        kafkaTemplate.send(appProperties.getKafkaStreaming().getInputTopic(), finalRecord);
    }
}
