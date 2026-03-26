## Quiz 1: Apache Kafka for Microservices Quiz

### Question 1:
**What is Apache Kafka primarily used for?**

- [ ] To store data  
- [x] Distributed event streaming  
- [ ] To build Spring Boot Microservices  

> **📘 i**  
> Apache Kafka is primarily used as a distributed event streaming platform. It excels in collecting, processing, storing, and integrating large volumes of data in real-time. Kafka allows different systems and applications to publish (produce) and subscribe to (consume) streams of events, making it highly suitable for event-driven architectures.

### Question 2:
**You can run multiple Apache Kafka servers in a cluster. True or False?**
- [x] True  
- [ ] False 

> **📘 i**  
> Apache Kafka is designed to run as a cluster comprising multiple servers or nodes. This clustered environment is a fundamental aspect of Kafka's architecture, allowing it to achieve high availability, fault tolerance, and scalability. By running multiple servers in a cluster, Kafka can distribute the load and data across different nodes. The setup also ensures that if one server goes down, others in the cluster can continue to handle requests and process data, thereby maintaining the system's resilience and reliability.

### Question 3:
**In the context of Apache Kafka, what is a Producer?**
- [ ] A server that manages Kafka clusters
- [x] A microservice that publishes events
- [ ] A storage system within Kafka

> **📘 i**  
> In Apache Kafka, a Producer is a microservice, application, or process that publishes (produces) events or messages to Kafka topics. Producers send data to Kafka brokers, which then store the data in topics.

### Question 4:
**In the context of Apache Kafka, what is a Consumer?**
- [ ] An application that stores data in Kafka topics
- [x] A microservice that consumes or reads events from Kafka topics
- [ ] A data replication mechanism in Kafka

> **📘 i**  
> In Apache Kafka, a Consumer is a microservice or a process that consumes or reads events or messages from Kafka topics. Consumers subscribe to one or more Kafka topics and process the stream or records produced to those topics.

### Question 5:
**In the context of Apache Kafka, what is a Broker?**
- [ ] A data processing tool within Kafka
- [x] A server that manages Kafka topics, facilitates writes to partitions, and handles replication
- [ ] A component that consumes data from topics

> **📘 i**  
> In Apache Kafka, a Broker is a server that performs several critical functions. It manages Kafka topics, handles the storage of data into topic partitions, manages replication of data for fault tolerance, and serves client requests (from both Producers and Consumers).

## Quiz 2: Kafka Topics, Partitions and Messages

### Question 1:
**What is a Kafka Topic primarily used for in the Kafka architecture?**
- [ ] Data processing and transformation
- [x] Organizing streams of messages for publish-subscribe systems
- [ ] Storing metadata about Kafka clusters

> **📘 i**  
> Kafka topics are used to group/organize and label your data streams, so that you can easily send and receive data from them. For example, you can have a topic for weather data, and another topic for traffic data. Producers can send data to any topic they want, and consumers can subscribe to any topic they are interested in.

### Question 2:
**What does a Partition within a Kafka Topic represent?**
- [ ] A separate Kafka cluster
- [x] A division in the topic for load balancing and parallel processing
- [ ] A unique identifier for message tracking

> **📘 i**  
> A topic is a category or a log for a stream of data. Partitions are used to split a topic into smaller pieces, so that more than one server or consumer can handle the data at the same time. This helps to balance the workload and speed up the processing.

### Question 3:
**You can increase the number of partitions in a Kafka topic to scale. But once set, partitions can't be reduced.**
- [x] True
- [ ] False

> **📘 i**  
> As per the latest update (4.2), the number of partitions can only be increased, decreasing the number of partitions is not possible.

### Question 4:
**How does Kafka ensure message ordering in a Topic Partition?**
- [ ] By timestamping each message
- [x] By appending messages in the order they are sent within each partition
- [ ] By the size of the message

> **📘 i**  
> Kafka appends messages in the order they are sent within each partition. This means that the first message sent to a partition will be the first message read from that partition, and so on. Kafka does not use timestamps, random algorithms, or message size to order messages in a partition. Kafka only cares about the arrival time of the messages from the producer.

### Question 5:
**Can a single Kafka Topic have multiple Partitions?**
- [x] Yes
- [ ] No

> **📘 i**  
> A Kafka topic is like a folder where you can store and organize your data. A Kafka Topic can be divided into multiple smaller folders called partitions, which can be stored on different servers. This helps to distribute the workload and speed up the processing.

### Question 6:
**How does Kafka ensure message ordering within a partition?**
- [ ] By using a global offset for all messages in topic
- [ ] By assuming a unique timestamp to each message in a topic
- [x] By using a key for each message and partitioning by the hash of the key

> **📘 i**  
> Kafka guarantees ordering within a partition by assigning sequential offsets to records. A key is an optional attribute of a message that determines which partition the message is sent to. For example, you can use a user ID as a key to ensure all messages for the same user go to the same partition. Within that partition, Kafka guarantees that messages are ordered by their arrival time from the producer.