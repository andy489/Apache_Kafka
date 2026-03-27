# Kafka Producer CLI

## Introduction

### Kafka CLI: `kafka-console-consumer.sh`

The main use of `kafka-console-producer.sh` script is to send a message to a particular Kafka topic.

- Send message with a Key
- Send message without a Key
- Send multiple messages from a file

**Example:**

`bin/kafka-console-producer.sh --broker-list localhost:9092 --topic my-topic`