package com.showmeyourcode.kafka.java.common;

public final class KafkaProperties {
    public static final String TOPIC = "java-example-topic";
    // If you register a new Schema, you need to use a different topic.
    public static final String TOPIC2 = "java-example-topic2";
    public static final String BOOTSTRAP_SERVERS = "localhost:9092";

    public static final String CONSUMER_GROUP_ID = "java-test-group";
    public static final String PRODUCER_CLIENT_ID = "JavaKafkaProducer";
    public static final String PRODUCER_AVRO_CLIENT_ID = "JavaKafkaAvroProducer";

    public static final String AVRO_SCHEMA_REGISTRY = "http://localhost:8081";

    private KafkaProperties() {
    }
}
