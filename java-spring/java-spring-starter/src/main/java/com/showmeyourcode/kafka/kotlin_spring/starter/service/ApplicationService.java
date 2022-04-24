package com.showmeyourcode.kafka.kotlin_spring.starter.service;

import java.util.UUID;

public class ApplicationService {

    private static final UUID instanceID = UUID.randomUUID();

    public static UUID getInstanceID() {
        return instanceID;
    }

    public static String getInstanceIDAsString() {
        return instanceID.toString();
    }
}
