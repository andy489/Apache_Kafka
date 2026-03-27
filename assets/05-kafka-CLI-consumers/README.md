# Kafka Consumer CLI

## Introduction

### Kafka CLI: `kafka-console-consumer.sh`

The main use of kafka-console-consumer.sh is to fetch and display messages from a Kafka topic to your terminal.

- Read new messages only
- Read all messages from the beginning

**Example:**

`bin/kafka-console-consumer.sh --topic my-topic --from-beginning --bootstrap-server localhost:9092`


