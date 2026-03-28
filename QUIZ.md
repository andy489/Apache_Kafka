## Quiz 1: Apache Kafka for Microservices Quiz

### Question 1:
**What is Apache Kafka primarily used for?**

- [ ] To store data  
- [x] Distributed event streaming  
- [ ] To build Spring Boot Microservices  

> **📘**  
> Apache Kafka is primarily used as a distributed event streaming platform. It excels in collecting, processing, storing, and integrating large volumes of data in real-time. Kafka allows different systems and applications to publish (produce) and subscribe to (consume) streams of events, making it highly suitable for event-driven architectures.

### Question 2:
**You can run multiple Apache Kafka servers in a cluster. True or False?**
- [x] True  
- [ ] False 

> **📘**  
> Apache Kafka is designed to run as a cluster comprising multiple servers or nodes. This clustered environment is a fundamental aspect of Kafka's architecture, allowing it to achieve high availability, fault tolerance, and scalability. By running multiple servers in a cluster, Kafka can distribute the load and data across different nodes. The setup also ensures that if one server goes down, others in the cluster can continue to handle requests and process data, thereby maintaining the system's resilience and reliability.

### Question 3:
**In the context of Apache Kafka, what is a Producer (microservice/client)?**
- [ ] A server that manages Kafka clusters
- [x] A microservice that publishes events
- [ ] A storage system within Kafka

> **📘**  
> In Apache Kafka, a Producer is a microservice, application, or process that publishes (produces) events or messages to Kafka topics. Producers send data to Kafka brokers, which then store the data in topics.

### Question 4:
**In the context of Apache Kafka, what is a Consumer (microservice/client)?**
- [ ] An application that stores data in Kafka topics
- [x] A microservice that consumes or reads events from Kafka topics
- [ ] A data replication mechanism in Kafka

> **📘**  
> In Apache Kafka, a Consumer is a microservice or a process that consumes or reads events or messages from Kafka topics. Consumers subscribe to one or more Kafka topics and process the stream or records produced to those topics.

### Question 5:
**In the context of Apache Kafka, what is a Broker?**
- [ ] A data processing tool within Kafka
- [x] A server that manages Kafka topics, facilitates writes to partitions, and handles replication
- [ ] A component that consumes data from topics

> **📘**  
> In Apache Kafka, a Broker is a server that performs several critical functions. It manages Kafka topics, handles the storage of data into topic partitions, manages replication of data for fault tolerance, and serves client requests (from both Producers and Consumers).

## Quiz 2: Kafka Topics, Partitions and Messages

### Question 1:
**What is a Kafka Topic primarily used for in the Kafka architecture?**
- [ ] Data processing and transformation
- [x] Organizing streams of messages for publish-subscribe systems
- [ ] Storing metadata about Kafka clusters

> **📘**  
> Kafka topics are used to group/organize and label your data streams, so that you can easily send and receive data from them. For example, you can have a topic for weather data, and another topic for traffic data. Producers can send data to any topic they want, and consumers can subscribe to any topic they are interested in.

### Question 2:
**What does a Partition within a Kafka Topic represent?**
- [ ] A separate Kafka cluster
- [x] A division in the topic for load balancing and parallel processing
- [ ] A unique identifier for message tracking

> **📘**  
> A topic is a category or a log for a stream of data. Partitions are used to split a topic into smaller pieces, so that more than one server or consumer can handle the data at the same time. This helps to balance the workload and speed up the processing.

### Question 3:
**You can increase the number of partitions in a Kafka topic to scale. But once set, partitions can't be reduced.**
- [x] True
- [ ] False

> **📘**  
> As per the latest update (4.2), the number of partitions can only be increased, decreasing the number of partitions is not possible. Partitions can be increased but not decreased (or decreasing is extremely complex and generally not supported).

### Question 4:
**How does Kafka ensure message ordering in a Topic Partition?**
- [ ] By timestamping each message
- [x] By appending messages in the order they are sent within each partition
- [ ] By the size of the message

> **📘**  
> Kafka appends messages in the order they are sent within each partition. This means that the first message sent to a partition will be the first message read from that partition, and so on. Kafka does not use timestamps, random algorithms, or message size to order messages in a partition. Kafka only cares about the arrival time of the messages from the producer.

### Question 5:
**Can a single Kafka Topic have multiple Partitions?**
- [x] Yes
- [ ] No

> **📘**  
> A Kafka topic is like a folder where you can store and organize your data. A Kafka Topic can be divided into multiple smaller folders called partitions, which can be stored on different servers. This helps to distribute the workload and speed up the processing.

### Question 6:
**How does Kafka ensure message ordering within a partition?**
- [ ] By using a global offset for all messages in topic
- [ ] By assuming a unique timestamp to each message in a topic
- [x] By using a key for each message and partitioning by the hash of the key.

> **📘**  
> Kafka guarantees ordering within a partition by assigning sequential offsets to records. A key is an optional attribute of a message that determines which partition the message is sent to. For example, you can use a user ID as a key to ensure all messages for the same user go to the same partition. Within that partition, Kafka guarantees that messages are ordered by their arrival time from the producer.

## Quiz 3: Apache Kafka Broker(s)

### Question 1:
**What is the primary role of a broker in Apache Kafka?**
- [x] To store, manage, and distribute messages in the Kafka messaging system
- [ ] To connect producers and consumers directly
- [ ] To encrypt and decrypt messages

> **📘**  
> A Kafka broker is an Apache Kafka component that stores, manages, and distributes messages in the Kafka messaging system. A broker hosts some set of partitions and handles incoming requests to write new events to those partitions or read events from them. A broker also handles replication of partitions between other brokers. This is the main function of a broker, as it enables Kafka to provide high-throughput, low-latency, and fault-tolerant data delivery.

### Question 2:
**How does Kafka ensure message durability?**
- [ ] By storing all messages in the system memory
- [x] By replicating messages across multiple brokers
- [ ] By compressing messages

> **📘**  
> Kafka ensures message durability through replication. Each message published to a Kafka topic can be replicated across multiple brokers. This means that even if one broker fails, the data is still available on other brokers. Replication is a key feature in Kafka that ensures high availability and durability of data.

### Question 3:
**What happens when a Kafka broker goes down?**
- [ ] All messages in the broker are immediately lost
- [x] Kafka redistributes the load to other brokers
- [ ] The entire Kafka cluster becomes inoperable

> **📘**  
> If a broker in a Kafka cluster is down, Kafka redistributes the workload among the remaining brokers. This includes reassigning the leader for the partitions that were on the failed broker. Kafka's distributed nature allows it to handle failures gracefully and maintain continuous operation.

### Question 4:
**What is the role of a Leader broker in a Kafka cluster?**
- [ ] To replicate data and follow instructions from followers
- [x] To handle all read and write requests for a specific partition
- [ ] To coordinate the configuration of the Kafka cluster

> **📘**  
> In Kafka, each partition of a topic has one leader broker. The leader handles all read and write requests for that partition. This centralizes the data management for that partition, ensuring consistency and efficiency in data handling.

### Question 5:
**What is the role of a Follower broker in a Kafka cluster?**
- [ ] To handle all read and write requests for the assigned partitions
- [x] To replicate data from the leader broker for the assigned partitions
- [ ] To manage the cluster configuration and broker registration

> **📘**  
> The primary role of a followe broker in a Kafka cluster is to replicate data from the leader broker for the assigned partitions. Follower brokers continually fetch data from the leader for each partition they are assigned to follow. This replication is critical for ensuring data redundancy and high availability. IN case the leader broker fails, one of these followers can be elected as the new leader, ensuring that the partitions remains available and no data is lost.

### Question 6:
**What happens when a leader broker for a partition becomes unavailable?**
- [ ] All data in the partition is temporary inaccesible
- [x] A follower broker is automatically elected as the new leader
- [ ] The partition is removed from the Kafka cluster

> **📘**  
> Kafka's high availability design includes the feature of automatic leader election. If the current leader broker for a partition becomes unavailable, one of the follower brokers, which has been replicating the data, is elected as the new leader. This ensures continuous availability and accessibility of the partition.

## Quiz 4: Kafka Producer

### Question 1:
**What is the primary function of a Kafka Producer?**
- [ ] To consume messages from Kafka topics
- [x] To write data to Kafka topics
- [ ] To replicate data across Kafka brokers

> **📘**  
> The main role of a Kafka Producer is to write data to Kafka topics. Producers are client applications that send streams of data to topics within the Kafka cluster. The producer determines which topic the data should be sent to and can also specify a key that influences the partition within the topic to which the data is sent.

### Question 2:
**How does a Kafka Producer determine which partition to send a message to?**
- [ ] Based on the consumer's subscription
- [x] Based on a provided message key or round-robin if no key is provided
- [ ] Randomly selects a partition for each message

> **📘**  
> Kafka Producers use message key to determine which partition a message should be sent to within a topic. If a key is provided, Kafka uses it to consistently assign all messages with that key to the same partition. If no key is provided, Kafka uses a round-robin method to distribute messages evenly across the available partitions.

### Question 3:
**What library is typically used to integrate Kafka functionality into a Spring Boot application?**
- [ ] JPA (Java Persistence API)
- [x] Spring for Apache Kafka
- [ ] Hibernate ORM

> **📘**  
> Spring for Apache Kafka is the library commonly used for integrating KAfka with a Spring Boot application. It simplifies the process of working with Kafka by providing a higher-level abstraction that fits well within the Spring ecosystem, making it easier to send and receive messages to and from Kafka topics.

### Question 4:
**In a Kafka Producer, what is the responsibility related to message serialization?**
- [ ] To compress the message for efficient storage
- [x] To serialize messages into a binary format for transmission
- [ ] To encrypt the message for secure transmission

> **📘**  
> One of the key responsibilities of a Kafka Producer is to serialize messages into a binary format before they are transmitted over the network to Kafka brokers. Serialization converts the message data, which might be in various formats (like strings, objects, etc.), into a standard binary format that can be efficiently transmitted and stored in Kafka.

### Question 5:
**In the context of the Orders Microservice acting as a Kafka Producer, what characterizes the synchronous communication style with the Kafka Broker?**
- [ ] The Orders Microservice sends and Order Created Event and proceeds with other tasks without waiting for a response from the Kafka Broker.
- [x] The Orders Microservice sends an Order Created Event and waits for an acknowledgment from the Kafka Broker before responding back to the Mobile Application.
- [ ]The Orders Microservice encrypts the Order Created Event for security before sending it to the Kafka Broker.

> **📘**  
> In synchronous communication, the sender (in this case, the Orders Microservice) waits for a response after sending a request (the Order Created Event) to ensure that the message was successfully stored in the Kafka Broker. This approach is used when the sender needs confirmation of successful message delivery, such as when creating an order, and it is cruical to know whether the operation was successful before proceeding.

### Question 6:
**How does the Kafka Producer behave when sending a User LoggedIn Event to the Kafka Broker asynchronously?**
- [ ] The Kafka Producer sends the User LoggedIn Event and waits for an acknowledgment from the Kafka Broker before continuing its execution.
- [x] The Kafka Producer sends the User LoggedIn Event and continues its execution right away without waiting for an acknowledgment from the Kafka Broker.
- [ ] The Kafka Producer encrypts the User LoggedIn Event and waits for the Kafka Broker to confirm its successful decryption.

> **📘**  
> In asynchronous communication, the Kafka Producer sends the User LoggedIn Event to the Kafka Broker and then immediately proceeds with its next task without waiting for a response from the Kafka Broker. This approach allows dor efficient processing, especially in scenarios where immediate confirmation of message delivery is not critical.

## Quiz 5: Kafka Producer Acknowledgements and Retries

### Question 1:
**What does the 'acks' configuration in a Kafka Producer specify?**
- [ ] The number of follower brokers that must replicate the message
- [ ] The number of retries the producer will attempt after a failure
- [x] The acknowledgment level required from the Kafka cluster for successful message delivery

> **📘**  
> The 'acks' configuration in Kafka Producer determines the level of acknowledgment required from the Kafka cluster for a message to be considered successfully delivered. For example, 'acks=0' means the producer will not wait for any acknowledgment, 'acks=1' means waiting for an acknowledgment from the leader broker only, and 'acks=all' means waiting for acknowledgments from all in-sync replicas.

### Question 2:
**What is the impact of setting 'acks=all' in a Kafka Producer configuration?**
- [ ] It increases message throughput by reducing the acknowledgment overhead
- [ ] It ensures higher data durability by requiring acknowledgment from all in-sync replicas
- [x] It encrypts all messages sent from the producer for added security

> **📘**  
> The 'acks=all' setting in a Kafka Producer configuration ensures higher data durability. This is because it requires acknowledgments from all in-cync replicas before considering the message successfully sent, reducing the likelihood of data loss in case of broker failure.

### Question 3:
**What is a possible drawback of setting 'acks=0' in a Kafka Producer?**
- [ ] It results in higher network usage due to frequent acknowledgment messages
- [x] It may lead to data loss as the producer does not wait for any acknowledgment from brokers
- [ ] It significantly reduces the producer's throughput due to waiting for acknowledgments

> **📘**  
> One of the main drawbacks of setting 'acks=0' in a kafka Producer is the increased risk of data loss. In this configuration, the producer sends messages without waiting for any acknowledgment from the brokers, which means it won't know if the message was not received or stored by the Kafka cluster.

### Question 4:
**What does the 'retries' configuration in a Kafka Producer control?**
- [x] The number of times the producer will attempt to resend a message after a send failure
- [ ] The total time the producer will spend retrying a message before giving up
- [ ] The speed at chich messages are sent to the Kafka broker

> **📘**  
> The 'retries' configuration in a Kafka Producer specifies the number of retry attempts the producer will make if a message send fails. This is crucial for ensuring delivery in the event of transient issues, such as temporary network problems of brief broker unavailability.

### Question 5:
**How does the 'retry.backoff.ms' setting affect Kafka Producer retries?**
- [ ] It sets the maximum size of the message batch for retries
- [x] It specifies the time to wait before attempting a message retry after a failure
- [ ] It determines the number of retries for each message

> **📘**  
> The 'retry.backoff.ms' setting in a Kafka Producer specifies the amount of time to wait before attempting another retry after a send failure. This backoff time is cruical to prevent overloading the network or broker with rapid successive retries.

## Quiz 6: Apache Kafka Consumer

### Question 1:
**What is the primary function of a Kafka Consumer?**
- [ ] To produce and send messages to Kafka topics
- [x] To read and process messages from Kafka topics
- [ ] To replicate messages across multiple Kafka brokers

> **📘**  
> The primary function of a Kafka Consumer is to read and process messages from kafka topics. Consumers subscribe to one or more Kafka topics and then process the stream of messages that are published to these topics.

### Question 2:
**What is the purpose of the `@KafkaHandler` annotation in a Kafka Consumer application?**
- [x] To define multiple methods in a listener class for different types of Kafka messages
- [ ] To specify the topic that the Kafka Consumer should listen to
- [ ] To increase the throughput of the Kafka Consumer by processing messages in parallel

> **📘**  
> The `@KafkaHandler` annotation is used within a Kafka listener class to define multiple methods for handling different types of messages. Each method annotated with `@KafkaHandler` can be tailored to process a specific type of message. This allows for a more organized and type-specific handling of messages within a single listener class, improving the clarity and maintainability of the code.

### Question 3:
**What distinguishes the `@KafkaHandler` annotation from the `@KafkaListener` annotation in a Kafka Consumer application?**
- [ ] `@KafkaHandler` is used to specify the Kafka topic the method should listen to, while `@KafkaListener` is used for handling specific message types.
- [ ] `@KafkaHandler` can be used at the class level, while `@KafkaListener` cannot.
- [x] `@KafkaHandler` is used to handle specific message types within a `@KafkaListener` class, whereas `@KafkaListener` is used to define the method that listens to messages from Kafka topics.

> **📘**  
> `@KafkaHandler` is used within a class that has `@KafkaListener` to handle specific message types, allowing for differentiated processing based on message content. The annotation cannot be used to specify the Kafka topic nor can it be placed above the class name. On the other hand, `@KafkaListener` is used to define methods (or classes) that specifically listen to messages from Kafka topics, and it can be used to specify the topic to listen to.

### Question 4:
**What is the difference between the `spring.kafka.consumer.keydeserializer` and `spring.kafka.consumer.value-deserializer` configuration properties in a Kafka Consumer application?**
- [ ] `keydeserializer` configures the consumer's polling behaviour, while `value-deseriualizer` configures the consumer's session timeout
- [ ] `keydeserializer` and `value-deseriualizer` are both used for configuring the consumer's message fetch size
- [x] `keydeserializer` specifies the deserializer class for message keys, while `value-deseriualizer` specifies the deserializer class for message values

> **📘**  
> The `spring.kafka.consumer.keydeserializer` property is used to specify the deserializer class for the keys of Kafka messages, and `spring.kafka.consumer.value-deserializer` is for specifying the deserializer class for the values of the messages. Deserializers are important as they convert the byte stream received from Kafka into a format that can be understood and processed by the consumer application.

### Question 5:
**In a Kafka Consumer application, what role does the `spring.kafka.consumer.properties.spring.json.trusted.packages` configuration play?**
- [x] It determines the packages allowed for deserializing JSON messages, ensuring only objects from known sources are processed
- [ ] It influences how JSON messages are mapped to Java objects, affecting data type conversions during deserialization
- [ ] It controls the handling of JSON payload type information, impacting the deserialization of complex data types

> **📘**  
> This property specifies the packages that are considered safe for JSON message deserialization. It's a security measure to prevent the application from deserializing objects from unknown or untrusted sources, thus mitigating potential security risks.

### Question 6:
**Can a single Kafka consumer consume messages from more than one topic?**
- [x] Yes, a consumer can be configured to consume messages from multiple topics simultaneously
- [ ] No, each consumer can only be linked to one topic at a time

> **📘**  
> A Kafka consumer can indeed be configured to consume messages from multiple topics. This allows for greater flexibility and efficient utilization of consumers, especially when dealing with a variety of topics that require similar processing logic.

## Quiz 7: Dead Letter Topic in Kafka

### Question 1:
**What is the primary purpose of a Dead Letter Topic in Apache Kafka?**
- [ ] To store messages that exceed the maximum size limit.
- [ ] To serve as a backup for all messages sent through Kafka.
- [x] To handle messages that could not be processed successfully.

> **📘**  
> The primary purpose of a Dead Letter Topic in Kafka is to handle messages that cannot be processed successfully by a consumer. This can include messages that cause exceptions during processing. The Dead Letter Topic provides a way to separate these problematic messages for further analysis or reprocessing, without interrupting the normal message flow.

### Question 2:
**When would a message be sent to a Dead Letter Topic in Kafka?**
- [ ] After it has been successfully processed by a consumer.
- [ ] When it has not been acknowledged by any consumer within a specified timeout.
- [x] If a consumer encounters an error while processing the message and cannot proceed.

> **📘**  
> A message is typically sent to a Dead Letter Topic when a consumer encounters an error while processing it, and cannot successfully process the message. This allows the message to be isolated and delt with separately ensuring that processing errors do not disrupt the normal flow of messages.

### Question 3:

**What is a common practice after a message is sent to a Dead Letter Topic in Kafka?**
- [ ] The message is automatically deleted from the original topic.
- [x] The message is usually analyzed and potentially reprocessed or corrected.
- [ ] The message is immediately resent to the consumer for reprocessing.

> **📘**  
> A common practice after a message ends up in a Dead Letter Topic is to analyze the message to understand why it failed and then decide on a course of action, which might include reprocessing or correcting the message.



## Quiz 8: Apache Kafka Consumer Groups

### Question 1:
**What is the primary function of Consumer Groups in Apache Kafka?**
- [ ] To increase message production speed from Kafka Producers.
- [x] To enable parallel processing of messages from a topic by multiple consumers.
- [ ] To encrypt messages for secure transmission between producers and consumers.

> **📘**  
> Consumer groups in Kafka allow multiple consumers to form a group and collaboratively process messages from a topic, enabling parallel processing. Each consumer in the group reads messages from one or more partitions of the topic, enhancing throughput and scalability.

### Question 2:
**What happens when a new consumer joins a consumer group in Kafka?**
- [ ] The consumer group stops consuming messages until the new consumer is fully integrated.
- [x] Kafka rebalances the topic partitions among all consumers in the group.
- [ ] The new consumer takes over all the message processing from the existing consumers.

> **📘**  
> When a new consumer joins a consumer group, Kafka triggers a rebalance of the topic partitions among all consumers in the group. This rebalancing ensures that the message load is distributed evenly across the consumers in the group.

### Question 3:
**How does Kafka manage message distribution to consumers within a consumer group?**
- [x] By sending each message to only one consumer in the group.
- [ ] By broadcasting every message to all consumers in the group.
- [ ] By storing messages in a central databa.se accessible to all consumers.

> **📘**  
> Kafka ensures message delivery to consumers in a group by assigning each message to only one consumer in the group. This approach prevents duplicate processing of the same message by multiple consumers in the group.

### Question 4:
**How does Apache Kafka assign partitions to consumers within a consumer group?**
- [x] Each consumer in a group is assigned a unique set of partitions from multiple topics.
- [ ] All consumers in a group are assigned the same set of partitions from a topic.
- [ ] Partitions are assigned randomly to consumers regardless of the group.

> **📘**  
> Kafka assigns each consumer in a consumer group a unique set of partitions from the topic they subscribe to. This partition assignment ensures that each partition is processed by only one consumer in the group, allowing for efficient parallel processing of the data.

### Question 5:
**What determines the number of partitions a consumer in a group can process in Kafka?**
- [ ] The total number of consumers in the Kafka cluster.
- [x] The number of partitions in the topic and the number of consumers in the consumer group.
- [ ] The consumer's processing speed and capacity.

> **📘**  
> The number of partitions that a consumer can process is determined by the total number of partitions in the topic and the number of consumers in the consumer group. Kafka strives to balance the partition assignment across consumers in a group to ensure even distribution and efficient processing.

### Question 6:
**What happens if the number of consumers in a Kafka Consumer Group exceeds the number of partitions in a topic?**
- [ ] Each consumer gets assigned multiple partitions.
- [x] Some consumers will sit idle and will not be assigned any partitions.
- [ ] The Kafka broker automatically increases the number of partitions.

> **📘**  
> If the number of consumers in a consumer group exceeds the number of partitions in the topic, some consumers will not be assigned any partitions. This is because Kafka ensures that each partition is only consumed by one consumer from the group.

### Question 7:
**What happens when a consumer in a Kafka Consumer Group shuts down?**
- [ ] The partition assigned to the shutdown consumer remain unprocessed.
- [x] Kafka rebalances the partition, reassigning them among the remaining consumers.
- [ ] The consumer group stops consuming messages until the consumer restarts.

> **📘**  
> When a consumer in a consumer group shuts down, Kafka triggers a rebalance of the partitions. The partitions previously assigned to the shutdown consumer are reassigned among the remaining active consumers in the group, ensuring continued processing of messages.

