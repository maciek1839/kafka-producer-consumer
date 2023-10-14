# Kafka - Producer and Consumer

- [Technology](#technology)
- [Getting started](#getting-started)
  - [Run Kafka locally](#run-kafka-locally)
  - [SSL/TLS Kafka configuration](#ssltls-kafka-configuration)
  - [Docker and Docker Compose commands](#docker-and-docker-compose-commands)
- [Kafka introduction](#kafka-introduction)
- [Confluent Platform](#confluent-platform)
- [Message Queue vs Streaming (Kafka)](#message-queue-vs-streaming-kafka)
- [Event-driven architecture](#event-driven-architecture)
- [FFault tolerance, high availability and Resiliency](#fault-tolerance-high-availability-and-resiliency)
- [What happens when a new consumer joins the group in Kafka? (rebalancing)](#what-happens-when-a-new-consumer-joins-the-group-in-kafka-rebalancing)
- [To consideration](#to-consideration)
- [Kafka best practises](#kafka-best-practises)
- [Maven commands](#maven-commands)
- [Useful links](#useful-links)

---

| Branch |                                                                                               Pipeline                                                                                               |                                                                                            Code coverage                                                                                             |                                          Test report                                           |                                 SonarCloud                                 |
|:------:|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------:|
| master | [![pipeline status](https://gitlab.com/ShowMeYourCodeYouTube/kafka-producer-consumer/badges/master/pipeline.svg)](https://gitlab.com/ShowMeYourCodeYouTube/kafka-producer-consumer/-/commits/master) | [![coverage report](https://gitlab.com/ShowMeYourCodeYouTube/kafka-producer-consumer/badges/master/coverage.svg)](https://gitlab.com/ShowMeYourCodeYouTube/kafka-producer-consumer/-/commits/master) | [link](https://showmeyourcodeyoutube.gitlab.io/kafka-producer-consumer/test-report/index.html) | [link](https://sonarcloud.io/organizations/showmeyourcodeyoutube/projects) |


## Technology

- Kafka
- Confluent Platform for Apache Kafka
- Maven
- Kotlin
- Java
- AWS Corretto
- Spring Boot 3.x (module `java-spring`) and standalone Kafka libraries (modules `kotlin` and `java`)
- Apache Avro
    - schema-based data serialization library
- Avro Kotlin
    - https://github.com/avro-kotlin/avro4k
    - Limitations: cannot generate a class from .avsc file (it allows on generating a schema based on a Kotlin class).
- Junit/Mockito/AssertJ + EmbeddedKafka

## Getting started

1. Download the Git repository
2. Enable annotation processing in your IDE (required by Lombok).
3. Install dependencies: `mvn clean install -U`
4. If your IDE doesn't see generated Avro classes, mark `java/java-kafka-producer/target/generated-sources` as `Generated Sources Root`.  
   <img src="docs/avro-generated-classes.png" width="400" height="200">  
5. Run Kafka and Zookeeper - see [Run Kafka locally](#run-kafka-locally)
6. When services are up, run a producer/consumer from any of modules:
   1. Standalone Kafka libraries:
       - `java|kotlin/**-kafka-producer`
       - `java|kotlin/**-kafka-consumer`
       - You can also run all at once using `java|kotlin/**-starter` modules for Java/Kotlin.
   2. Spring Boot application:
      - `spring/java-spring`
      - You can run the module many times and change the default port.
7. Follow console output and check the behaviour.
8. Open the Confluent Platform dashboard: `http://localhost:9021/`  
   <img src="docs/control-center.png" width="800" height="500"> 

### Run Kafka locally

Run Kafka and Zookeeper using Docker Compose.
- `docker-compose up -d --remove-orphans`
  - The default file taken by the command is `docker-compose.yml`

### Docker and Docker Compose commands

If you don't use IDE plugins, here you can find useful Docker commands:
- Show all containers
  - `docker container list`
- Stop a Docker container
  - `docker container stop [container_id]`
- Remove a Docker container
  - `docker container rm [container_id]`
- Build Docker Compose with the default file docker-compose.yml and remove previous containers
  - `docker-compose up -d --remove-orphans`
- Show Docker Compose containers
  - `docker-compose ps`
- Stop all running containers
  - `docker stop $(docker ps -a -q)`
- Delete all stopped containers
  - `docker rm $(docker ps -a -q)`

Reference: <https://www.codenotary.com/blog/extremely-useful-docker-commands/>

### SSL/TLS Kafka configuration

Generate certificates using Confluent script -  [instruction](ssl-tls-example/README-SSL-TLS.md).

Complete TLS/SSL Kafka configuration - [instruction](./secrets/README-SECRETS.md)

## Kafka introduction

Apache Kafka is a distributed event store and stream-processing platform. It is an open-source software platform developed by the Apache Software Foundation written in Scala and Java. The project aims to provide a unified, high-throughput, low-latency platform for handling real-time data feeds.  

<https://en.wikipedia.org/wiki/Apache_Kafka>  
<https://kafka.apache.org/>

---

Below there are many design/conceptual diagrams showing how Kafka works. In order to understand them, let's explain basic terminology.

- Record: Producer sends messages to Kafka in the form of records. A record is a key-value pair. It contains the topic name to be sent and other attributes which are optional e.g. partition number or key. Kafka broker keeps records inside topic partitions. Records sequence is maintained at the partition level. You can define the logic on which basis partition will be determined.
  - Kafka records are in a key/value format, where the keys can be null.
  - If no partition/key is specified, Kafka will use a round-robin partition assignment.
  - If the ordering of the messages matters for you, then the key is important. Let’s say you have a Kafka topic where you want to send order status. If you receive several status updates about the same order - like “prepared”, “shipped”, and “delivered”, you want to make sure that the applications consumes these statutes in the right order. 
  - Kafka can guarantee ordering only inside the same partition, and it is therefore important to be able to route correlated messages into the same partition. To do so you need to specify a key for each message and Kafka will put all messages with the same key in the same partition.
  - Another example, if you have cars and you want to get all the GPS positions in order for that particular car then you need to make sure to have your message key set as the unique identifier for your care order id as the message key makes perfect sense.
  - References
    - https://forum.confluent.io/t/what-should-i-use-as-the-key-for-my-kafka-message/312
    - https://www.geeksforgeeks.org/apache-kafka-message-keys/
    - https://silverback-messaging.net/concepts/broker/kafka/kafka-partitioning.html
    - [Understanding Kafka Topic Partitions](https://medium.com/event-driven-utopia/understanding-kafka-topic-partitions-ae40f80552e8)
- Topic: Producer writes a record on a topic and the consumer listens to it. A topic can have many partitions but must have at least one.
- Partition: A topic partition is a unit of parallelism in Kafka, i.e. two consumers cannot consume messages from the same partition at the same time. A consumer can consume from multiple partitions at the same time.
  - A single message sent to a particular topic will be routed to one of partitions. Remember the message is not replicated to all partitions. The message can ba replicated only across brokers contains the same topic (see a diagram below).
  - Kafka can replicate partitions across a configurable number of Kafka servers which is used for fault tolerance. Each partition has a leader server and zero or more follower servers. Leaders handle all read and write requests for a partition. Followers replicate leaders and take over if the leader dies. [Reference](https://cloudurable.com/blog/kafka-architecture-topics/index.html)
- Offset: A record in a partition has an offset associated with it. Think of it like this: partition is like an array; offsets are like indexes.
- Producer: Creates a record and publishes it to the broker.
- Consumer: Consumes records from the broker.
  - Messages are not removed once a consumer acknowledge the message. Purging of messages in Kafka is done automatically by either specifying a retention time for a topic or by defining a disk quota for it so for your case of one 5GB file, this file will be deleted after the retention period you define has passed, regardless of if it has been consumed or not. [Reference 1](https://gist.github.com/slgithub/28e388ae361d357c9dcf) / [Reference 2](https://stackoverflow.com/questions/28586008/delete-message-after-consuming-it-in-kafka)
  - Consumers can:
    - Specify Topic(s) to read data - Subscribe/Assign
    - Read data (sequentially)  - Poll
    - Optional reposition - Seek
    - Save position (ack processed) - Commit

If you are looking for a book about Kafka, let's have a look on ``Kafka: The Definitive Guide`` which you can get here: <https://www.confluent.io/resources/kafka-the-definitive-guide/>

<img src="docs/kafka-clusters-kafka-clusters-architecture-diagram.png"  width="1100" height="1000">

[Reference link](https://hevodata.com/learn/kafka-clusters/)

<img src="docs/kafka-partitions.png"  width="900" height="400">

[Reference link](https://jack-vanlightly.com/blog/2018/9/2/rabbitmq-vs-kafka-part-6-fault-tolerance-and-high-availability-with-kafka)

<img src="docs/kafka-replication-factor-2.png"  width="900" height="400">

Kafka Replication Factor refers to the multiple copies of data stored across several Kafka brokers. In the above diagram consisting of three brokers, the replication factor is 2.  
[Reference link](https://blog.clairvoyantsoft.com/steps-to-increase-the-replication-factor-of-a-kafka-topic-a516aefd7e7e)

<img src="docs/multiple-consumer-groups.png"  width="800" height="500">

[Reference link](https://medium.com/@jhansireddy007/how-to-parallelise-kafka-consumers-59c8b0bbc37a)

<img src="docs/producer.png"  width="800" height="700">

[Reference link](https://stackoverflow.com/questions/38024514/understanding-kafka-topics-and-partitions)

<img src="docs/consumer-offset.png"  width="700" height="300">

[Reference link](https://kafka.apache.org/documentation/#intro_topics)

<img src="docs/log_anatomy.png" width="700" height="300">

[Reference link](https://kafka.apache.org/081/documentation.html)

<img src="docs/apache-kafka-partitions-topics.png"  width="800" height="400">

[Reference link](https://www.cloudkarafka.com/blog/part1-kafka-for-beginners-what-is-apache-kafka.html)

<img src="docs/partitions-kafka.png"  width="800" height="400">  

[Reference link](https://docs.datastax.com/en/kafka/doc/kafka/kafkaHowMessages.html)

## Confluent Platform

The Confluent setup provides web UI for the whole Kafka platform.  
More information you can find in this guideline: <https://docs.confluent.io/platform/current/quickstart/ce-docker-quickstart.html>

<img src="docs/confluent-platform.png"  width="1100" height="900">

[Reference link](https://docs.confluent.io/platform/current/platform.html)

- **Confluent Control Center** is a web-based tool for managing and monitoring Apache Kafka®. Control Center provides a user interface that allows developers and operators to get a quick overview of cluster health, observe and control messages, topics, and Schema Registry, and to develop and run ksqlDB queries. See more: <https://docs.confluent.io/platform/current/control-center/index.html>

<img src="docs/kafka_example_cc.png"  width="700" height="500">

- **Confluent Schema Registry** provides a serving layer for your metadata. It provides a RESTful interface for storing and retrieving your Avro®, JSON Schema, and Protobuf schemas. See more: <https://docs.confluent.io/platform/current/schema-registry/index.html>
- **ksqlDB** is the streaming SQL engine for Kafka. It provides an easy-to-use yet powerful interactive SQL interface for stream processing on Kafka, without the need to write code in a programming language such as Java or Python.

## Message Queue vs Streaming (Kafka)

`A message queue`, sometimes called a point-to-point communication, is fairly straightforward. A message queue can have one or more consumers and/or producers. In a message queue with multiple consumers, the queue will attempt to distribute the messages evenly across them, with the guarantee being that every message will only be delivered once.

<img src="docs/kafka-consumer-api.png" width="900" height="200">

`A streaming broker` is different from a message queue for many reasons. For starters, messages are organized into log files or topics. One or more consumers can subscribe to a log file or topic to receive all messages that come through that stream. With proper setup, a streaming broker will deliver the same message to every subscriber, in a specific order. This is often described as a publish-subscribe pattern.

While many consumers may be active, queues only deliver messages to a single consumer (typically whichever consumer is available to receive it first) before removing that message from the queue. Meanwhile, streaming brokers send the same message to every subscriber of that log file.

In a queue, once a message is delivered, it's gone forever. To reprocess a message, you have to have a backup, like a batch layer, so that you can put it back into the queue. In comparison, a streaming broker uses a distributed log file, so consumers can move backward and forward within that file to re-process messages they've already received on command.

<img src="docs/kafka-streams-api.png" width="900" height="200">

- https://blog.iron.io/message-queue-vs-streaming/
- https://www.baeldung.com/java-kafka-streams-vs-kafka-consumer


### Reprocess failed messages

```
We are implementing a Kafka Consumer using Spring Kafka. 
As I understand correctly if processing of a single message fails, there is the option to :
- Don't care and just ACK
- Do some retry handling using a RetryTemplate
- If even this doesn't work do some custom failure handling using a RecoveryCallback

I am wondering what your best practices are for that. 
I think of simple application exceptions, such as DeserializationException (for JSON formatted messages) 
or longer local storage downtime, etc.
```
Reference: https://stackoverflow.com/questions/45883794/kafka-consumes-unprocessable-messages-how-to-reprocess-broken-messages-later

- One example is Spring Cloud Stream, which can be configured to publish failed messages to another topic errors.foo; users can then copy them back to the original topic to try again later. This logic is done in the recovery callback.  It's called a "Dead letter queue".
- By using the error handling deserialiser, you can capture the failed message and route it to a dead letter topic for later recovery. This allows you to have access to the failed message and take appropriate actions to fix and reprocess it. [Reference](https://oso.sh/blog/reprocess-messages-in-kafka/)
- If any uncaught exception happens during *Kafka Streams processing*, your stream will change status to ERROR and stop consuming incoming messages for partition on which error occurred. You need to catch exceptions by yourself. Retries could be achieved either: 
  - using Spring RetryTemplate to invoke external microservice (but keep in mind that you will have delays of consuming messages from a specific partition),
  - push failed message into another topic for later reprocessing (as you suggested)
- You can implement some kind of kafkaProducer that will be a messageFailureHandler and with it you can send all the failed messages to a dedicated kafka topic. If you are familiar with the concept of dead-letter-queue in kafka-connect, it kind of the same (besides in kafka-connect it's only a matter of configuration). [Reference](https://stackoverflow.com/questions/62586094/handling-failed-messages-using-kafka-streams-processor-api)

## Event-driven architecture

```text
Event-driven architecture is a software architecture and model for application design. 
With an event-driven system, the capture, communication, processing, and persistence of events are the core structure of the solution. 
This differs from a traditional request-driven model.
```

Event-driven architecture (EDA) is made up of event producers and event consumers. 
An event producer detects or senses an event and represents the event as a message. 
It does not know the consumer of the event, or the outcome of an event.

- An EDA is loosely coupled or entirely decoupled.
- An EDA uses asynchronous messaging, typically pub/sub.
- An EDA enables parallelism and fault tolerance, ensuring the system remains responsive and resilient
  - Fault tolerance refers to the ability of a system (computer, network, cloud cluster, etc.) to continue operating without interruption when one or more of its components fail.
  - Resilience can be defined as the capability to recover when stressed by load (more requests for service), attacks (either accidental through a bug, or deliberate through intention), and failure of any component in the workload’s components.
- An EDA provides an ability to build processing pipeline.
  - Data pipelines are data processing steps where raw data is ingested from various data sources and then ported to data store, like a data lake or data warehouse, for analysis
- An EDA thanks to availability of sophisticated event brokers reduce code complexity.
- An EDA provides proven [Enterprise Integration Patterns](https://www.enterpriseintegrationpatterns.com/).

<img src="docs/event-driven-example.png" width="900" height="500">

References:
- https://stackoverflow.com/questions/72299824/event-driven-architecture-backend-services-with-two-interfaces-interface-type/
- https://developer.ibm.com/articles/eda-and-microservices-architecture-best-practices/
- https://databand.ai/blog/7-data-pipeline-examples-etl-data-science-ecommerce-and-more/

### Event-driven architecture models

An event driven architecture may be based on either a pub/sub model or an event stream model.

- `Pub/sub model`
  - This is a messaging infrastructure based on subscriptions to an event stream. With this model, after an event occurs, or is published, it is sent to subscribers that need to be informed.
- `Event streaming model`
  - With an event streaming model, events are written to a log. Event consumers don’t subscribe to an event stream. Instead, they can read from any part of the stream and can join the stream at any time.

```text
Message Queues: MQs are a point-to-point communication system where messages are sent to a single receiver, ensuring ordered and reliable delivery.
Publish-Subscribe: In Pub/Sub systems, messages are sent to multiple subscribers simultaneously, promoting decoupling and scalability.
```

<img src="docs/pubsub-vs-streaming.png" width="800" height="400">

Ref: <https://metamug.com/article/distributed-systems/event-streaming-pub-sub.html/>

References:
- https://cloud.google.com/solutions/event-driven-architecture-pubsub/
- https://blog.iron.io/message-queue-vs-publish-subscribe/
- https://blog.ippon.tech/event-driven-architecture-getting-started-with-kafka-part-1/
- https://hevodata.com/learn/kafka-event-driven-architecture/
- https://www.redhat.com/en/topics/integration/what-is-event-driven-architecture
- https://learn.microsoft.com/en-us/azure/architecture/guide/architecture-styles/event-driven
- (recommended) **https://microservices.io/patterns/data/event-driven-architecture.html**

## Fault tolerance, high availability and resiliency

```text
Fault tolerance is the property that enables a system to continue
operating properly in the event of the failure of one or more faults
within some of its components.
```

```text
IT resilience is the ability of an organization to maintain 
acceptable service levels when there is a disruption of business operations, 
critical processes, or your IT ecosystem.
```

```text
High availability (HA) is the ability of a system to operate continuously without failing for a designated period of time. 
```

- Kafka is sending ACK when a message is completely replicated to all brokers accordingly to settings. The min. insync. Replicas is a config on the broker that denotes the minimum number of in-sync replicas required to exist for a broker to allow acks=all requests. If not enough brokers are available, a message cannot be accepted by Kafka.
- The message will be in a buffer for limited time. If it's not published during this time, TimeoutException is thrown.
- An in-sync replica (ISR) is a broker that has the latest data for a given partition. A leader is always an in-sync replica.

```text
void sendSync() throws InterruptedException {
    try {
        Future<RecordMetadata> send = kafkaProducer.send(record);
        log.info("Record metadata: {}",send.get().toString());
    } catch(Exception e){
        if (e.getCause() instanceof TopicAuthorizationException){
            log.error("Not allowed to publish to topic!");
            throw new RuntimeException(e);
        }
        if (e.getCause() instanceof TimeoutException){
            log.error("TimeoutException: "+ e.getMessage());
            if (retries > MAX_RETRIES){
                backoffTime = MAX_TIME;
                log.info("Timeout has been increased to {}", MAX_TIME);
            } else {
                  retries++;
                  log.info("Retries currently at {}", retires);
            }
            Thread.sleep(backoffTime);
            sendSync(record);
        }
    }
}
```

Example error:
```text
(...) retrying (2147483630 attempts left). Error: NOT_ENOUGH_REPLICAS
```

References:
- https://www.datacore.com/blog/availability-durability-reliability-resilience-fault-tolerance/
- https://www.macrometa.com/distributed-data/high-availability-vs-fault-tolerance

## What happens when a new consumer joins the group in Kafka? (rebalancing)

What is a rebalancing? All messages on a topic are spread out among the members of consumer group. Every consumer has its set of partitions assigned exclusively to it and rebalancing is all about maintaining all partitions assigned to active consumers.

When one consumer dies Kafka needs to reassign orphaned partitions to the rest of the consumers. Similarly, when a new consumer joins the group Kafka needs to free up some partitions and assign them to the new consumers (if it can).

![Rebalancing](./docs/rebalancing.png)  

Reference: https://chrzaszcz.dev/2019/06/kafka-rebalancing/

## To consideration

- Async and Sync usage and implications.
- Sending batches vs single record.
- How to ensure message order? Sending one by one and blocking the request (GET on Future) instead of async batches which do not guarantee order.
- Closing the producer.
- Important parameters and implications
  - Ordering
  - Timeout and what does that mean?
  - How/When to retry?
- Threading
  - No thread safe, all I/O happen on caller thread, need synchronization if called from the different thread.
- Polling
  - Keep polling to maintain membership
  - max.poll.interval.ms (default 300s)
  - max.poll.records (default 500)
  - Server side configuration that prevents max.poll.interval.ms
  - "Backpressure can cause consumer to be kicked out"
- Consumer Rebalancing "Rebalancing is the process where a group of consumer instances (belonging to the same group) co-ordinate to own a mutually exclusive set of partitions of topics that the group is subscribed to.
  - Consumer with no heartbeat or not polling within the interval specified.
  - Group coordinator (Broker) not available.
  - Assignment strategy can be configured on client side.
- Offset and Commit
  - Commit can fail - especially if you don't own the partition any more.
  - Offset might not be continuous (example compact strategy is used for topic, or when producer uses transactions).
  - Commit expires after 2 weeks.
  - Default has enable.auto.commit=true
- Subscribe vs Assign
  - Subscribe use group coordination.
  - Commit has additional cost using Subscribe (need to check group ownership).
  - Use Assign lose coordination behaviour but much faster and resilient to lead re-election/coordination failure.
- Clean-up and close
  - Can wake up consumer in Polling (thread safe).
- Important settings
  - ClientId
  - IsolationLevel e.g. read_uncommitted
  - GroupId
  - AutoOffsetResetConfig e.g. earliest
  - EnableAutoCommitConfig
  - MaxPollRecordConfig e.g. 10
  - MaxPollIntervalMsConfig e.g. 15000
  - PartitionAssignmentStrategyConfig e.g. RangeAssignor

## Kafka best practises

- Amazon Managed Streaming for Apache Kafka
  - https://docs.aws.amazon.com/msk/latest/developerguide/bestpractices.html
- Kafka Best Practices-Topic, Partitions, Consumers, Producers and Brokers
  - https://cloudinfrastructureservices.co.uk/kafka-best-practices-topic-partitions-consumers-producers-and-brokers/
- Apache Kafka: Ten Best Practices to Optimize Your Deployment
  - https://www.infoq.com/articles/apache-kafka-best-practices-to-optimize-your-deployment/

## Maven commands

- mvn clean install -DskipTests

### Format files using ktlint

Use a command: ``mvn ktlint:format`` in `kotlin` module.

*Add a JVM option `--add-opens=java.base/java.lang=ALL-UNNAMED` if you encounter any access errors running the command.*

Ref: https://gantsign.com/ktlint-maven-plugin/usage.html

### Release a new version

Run a Maven command using Maven Release plugin:
```text
mvn release:prepare
```

If you want to only update versions (not recommended), use below command:
```text
mvn release:update-versions -DautoVersionSubmodules=true
```

## Useful links

- https://kotlinlang.org/docs/maven.html
- https://github.com/only2dhir/kafkaexample
- https://dzone.com/articles/kafka-producer-and-consumer-example
