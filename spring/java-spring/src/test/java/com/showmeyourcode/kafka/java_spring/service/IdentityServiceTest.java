package com.showmeyourcode.kafka.java_spring.service;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class IdentityServiceTest {

    private class FakeUuidProvider implements UuidProvider {
        @Override
        public UUID uuid() {
            return UUID.fromString("00000000-0000-0000-0000-000000000000");
        }
    }

    @Test
    void shouldSetApplicationIdWhenServiceIsCreated(){
        var clasUnderTest = new IdentityService(new FakeUuidProvider());

        assertThat(clasUnderTest.getId()).isEqualTo("00000000-0000-0000-0000-000000000000");
    }
}


