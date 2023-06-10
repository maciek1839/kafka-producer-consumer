package com.showmeyourcode.kafka.java_spring.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KafkaMessage {

    private String producerId;
    private int messageId;
}
