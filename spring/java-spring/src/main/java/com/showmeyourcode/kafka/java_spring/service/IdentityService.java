package com.showmeyourcode.kafka.java_spring.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IdentityService {

    private final UUID applicationId;

    public IdentityService(UuidProvider uuidProvider){
        this.applicationId = uuidProvider.uuid();
    }

    public String getId() {
        return applicationId.toString();
    }
}
