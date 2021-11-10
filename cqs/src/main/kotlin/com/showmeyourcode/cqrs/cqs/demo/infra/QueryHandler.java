package com.showmeyourcode.cqrs.cqs.demo.infra;

public interface QueryHandler<R, C extends Query<R>> {
    R handle(C query);
}
