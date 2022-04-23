package com.showmeyourcode.cqrs_eventsourcing.demo.domain;

public interface CommandHandler<R, C extends  Command<R>> {
    void handle(C command);
}
