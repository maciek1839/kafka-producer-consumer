package com.showmeyourcode.cqrs.demo.infra;

public interface QueryHandler<R, C extends Query<R>> {
    R handle(C query);
}
