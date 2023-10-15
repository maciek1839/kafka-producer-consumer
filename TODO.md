# TODOs

- Add from https://learn.microsoft.com/en-us/azure/architecture/guide/architecture-styles/event-driven
- Offset examples
  - Add business context to consumer and producer (warehouse?)
- Kafka Streams example (Spring) - add retry logic.
- Kafka - add retry logic.
- Use Spring Boot POM to manage dependencies
- SonarLint warnings
- Rearrange examples from all modules to play together into an Event Driven System (define business context)
- (Optional) Implementation for SSL/TLS Kafka
- (Optional) Add TestContainers tests for starters
- fix tests (a broker not available?)

---
handle errors? retry?

2023-10-15T00:32:03.061+11:00  INFO 660 --- [ntainer#0-0-C-1] c.s.k.j.s.m.KafkaConsumerService         : Consumer (574cb3ed-de18-47a9-9f75-e13b8d9c5f5f, partition: 0) received message: KafkaMessage(producerId=574cb3ed-de18-47a9-9f75-e13b8d9c5f5f, messageId=5)
2023-10-15T00:32:03.930+11:00  INFO 660 --- [-StreamThread-1] o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=spring-streaming-app-7266a595-64d9-4065-b3e5-808715e1523f-StreamThread-1-consumer, groupId=spring-streaming-app] Successfully joined group with generation Generation{generationId=4, memberId='spring-streaming-app-7266a595-64d9-4065-b3e5-808715e1523f-StreamThread-1-consumer-0d1dfa0f-9016-4353-ab28-ced053e006ff', protocol='stream'}
2023-10-15T00:32:03.944+11:00 ERROR 660 --- [-StreamThread-1] o.a.k.s.p.i.InternalTopicManager         : stream-thread [main] Existing internal topic spring-streaming-app-counts-repartition has invalid partitions: expected: 2; actual: 1. Use 'kafka.tools.StreamsResetter' tool to clean up invalid topics before processing.
2023-10-15T00:32:03.946+11:00  INFO 660 --- [-StreamThread-1] o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=spring-streaming-app-7266a595-64d9-4065-b3e5-808715e1523f-StreamThread-1-consumer, groupId=spring-streaming-app] Request joining group due to: rebalance failed due to 'Existing internal topic spring-streaming-app-counts-repartition has invalid partitions: expected: 2; actual: 1. Use 'kafka.tools.StreamsResetter' tool to clean up invalid topics before processing.' (StreamsException)
2023-10-15T00:32:03.947+11:00 ERROR 660 --- [-StreamThread-1] org.apache.kafka.streams.KafkaStreams    : stream-client [spring-streaming-app-7266a595-64d9-4065-b3e5-808715e1523f] Encountered the following exception during processing and the registered exception handler opted to SHUTDOWN_CLIENT. The streams client is going to shut down now.

org.apache.kafka.streams.errors.StreamsException: Existing internal topic spring-streaming-app-counts-repartition has invalid partitions: expected: 2; actual: 1. Use 'kafka.tools.StreamsResetter' tool to clean up invalid topics before processing.
at org.apache.kafka.streams.processor.internals.InternalTopicManager.validateTopics(InternalTopicManager.java:586) ~[kafka-streams-3.4.1.jar:na]
at org.apache.kafka.streams.processor.internals.InternalTopicManager.makeReady(InternalTopicManager.java:402) ~[kafka-streams-3.4.1.jar:na]
at org.apache.kafka.streams.processor.internals.RepartitionTopics.setup(RepartitionTopics.java:88) ~[kafka-streams-3.4.1.jar:na]
at org.apache.kafka.streams.processor.internals.StreamsPartitionAssignor.prepareRepartitionTopics(StreamsPartitionAssignor.java:518) ~[kafka-streams-3.4.1.jar:na]
at org.apache.kafka.streams.processor.internals.StreamsPartitionAssignor.assign(StreamsPartitionAssignor.java:388) ~[kafka-streams-3.4.1.jar:na]
at org.apache.kafka.clients.consumer.internals.ConsumerCoordinator.onLeaderElected(ConsumerCoordinator.java:696) ~[kafka-clients-3.4.1.jar:na]
at org.apache.kafka.clients.consumer.internals.AbstractCoordinator.onLeaderElected(AbstractCoordinator.java:726) ~[kafka-clients-3.4.1.jar:na]
at org.apache.kafka.clients.consumer.internals.AbstractCoordinator.access$1000(AbstractCoordinator.java:112) ~[kafka-clients-3.4.1.jar:na]
at org.apache.kafka.clients.consumer.internals.AbstractCoordinator$JoinGroupResponseHandler.handle(AbstractCoordinator.java:630) ~[kafka-clients-3.4.1.jar:na]
at org.apache.kafka.clients.consumer.internals.AbstractCoordinator$JoinGroupResponseHandler.handle(AbstractCoordinator.java:593) ~[kafka-clients-3.4.1.jar:na]
at org.apache.kafka.clients.consumer.internals.AbstractCoordinator$CoordinatorResponseHandler.onSuccess(AbstractCoordinator.java:1260) ~[kafka-clients-3.4.1.jar:na]
at org.apache.kafka.clients.consumer.internals.AbstractCoordinator$CoordinatorResponseHandler.onSuccess(AbstractCoordinator.java:1235) ~[kafka-clients-3.4.1.jar:na]
at org.apache.kafka.clients.consumer.internals.RequestFuture$1.onSuccess(RequestFuture.java:206) ~[kafka-clients-3.4.1.jar:na]
at org.apache.kafka.clients.consumer.internals.RequestFuture.fireSuccess(RequestFuture.java:169) ~[kafka-clients-3.4.1.jar:na]
at org.apache.kafka.clients.consumer.internals.RequestFuture.complete(RequestFuture.java:129) ~[kafka-clients-3.4.1.jar:na]
at org.apache.kafka.clients.consumer.internals.ConsumerNetworkClient$RequestFutureCompletionHandler.fireCompletion(ConsumerNetworkClient.java:617) ~[kafka-clients-3.4.1.jar:na]
at org.apache.kafka.clients.consumer.internals.ConsumerNetworkClient.firePendingCompletedRequests(ConsumerNetworkClient.java:427) ~[kafka-clients-3.4.1.jar:na]
at org.apache.kafka.clients.consumer.internals.ConsumerNetworkClient.poll(ConsumerNetworkClient.java:312) ~[kafka-clients-3.4.1.jar:na]
at org.apache.kafka.clients.consumer.internals.ConsumerNetworkClient.poll(ConsumerNetworkClient.java:251) ~[kafka-clients-3.4.1.jar:na]
at org.apache.kafka.clients.consumer.KafkaConsumer.pollForFetches(KafkaConsumer.java:1307) ~[kafka-clients-3.4.1.jar:na]
at org.apache.kafka.clients.consumer.KafkaConsumer.poll(KafkaConsumer.java:1243) ~[kafka-clients-3.4.1.jar:na]
at org.apache.kafka.clients.consumer.KafkaConsumer.poll(KafkaConsumer.java:1216) ~[kafka-clients-3.4.1.jar:na]
at org.apache.kafka.streams.processor.internals.StreamThread.pollRequests(StreamThread.java:1007) ~[kafka-streams-3.4.1.jar:na]
at org.apache.kafka.streams.processor.internals.StreamThread.pollPhase(StreamThread.java:955) ~[kafka-streams-3.4.1.jar:na]
at org.apache.kafka.streams.processor.internals.StreamThread.runOnce(StreamThread.java:762) ~[kafka-streams-3.4.1.jar:na]
at org.apache.kafka.streams.processor.internals.StreamThread.runLoop(StreamThread.java:613) ~[kafka-streams-3.4.1.jar:na]
at org.apache.kafka.streams.processor.internals.StreamThread.run(StreamThread.java:575) ~[kafka-streams-3.4.1.jar:na]
