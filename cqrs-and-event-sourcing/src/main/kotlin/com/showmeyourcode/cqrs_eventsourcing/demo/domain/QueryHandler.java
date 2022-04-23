package com.showmeyourcode.cqrs_eventsourcing.demo.domain;

public interface QueryHandler<R, C extends Query<R>> {
    R handle(C query);
}
