package com.showmeyourcode.cqrs.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CqrsDemoApplication

fun main(args: Array<String>) {
	runApplication<CqrsDemoApplication>(*args)
}
