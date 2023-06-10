package com.showmeyourcode.kafka.java_spring.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RandomUuidProvider implements UuidProvider {
    @Override
    public UUID uuid() {
        return UUID.randomUUID();
    }
}
