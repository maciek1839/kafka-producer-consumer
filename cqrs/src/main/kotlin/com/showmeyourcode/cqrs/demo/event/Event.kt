package com.showmeyourcode.cqrs.demo.event

import org.springframework.context.ApplicationEvent

abstract class Event(source: Any) : ApplicationEvent(source)
