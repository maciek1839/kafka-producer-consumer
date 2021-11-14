package com.showmeyourcode.cqrs.demo.infra;

public interface CommandHandler<R, C extends  Command<R>> {
    R handle(C command);
}
