package com.showmeyourcode.kafka.kotlin_spring.starter.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KafkaMessage {

    private String producerId;
    private int messageId;
}
