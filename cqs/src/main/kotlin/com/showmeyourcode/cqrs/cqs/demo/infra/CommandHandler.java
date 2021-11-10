package com.showmeyourcode.cqrs.cqs.demo.infra;

public interface CommandHandler<R, C extends  Command<R>> {
    R handle(C command);
}
