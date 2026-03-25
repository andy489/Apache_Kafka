# Useful Commands

## Docker

#### Start a docker thread
`open -a docker`

#### Start the cluster
`docker compose up -d`


#### View logs for a specific broker
`docker logs kafka-1 -f`

#### Stop the cluster
`docker compose down`

#### Enter kafka-1 container
`docker exec -it kafka-1 bash`

## Kafka

### All kafka scripts (cli) are in `/opt/kafka/bin`

#### List topics
`./kafka-topics.sh --bootstrap-server localhost:9192 --list`

#### Or using the external listener
`./kafka-topics.sh --bootstrap-server localhost:9091 --list`

#### Describe a specific topic (replace with your topic name)
`./kafka-topics.sh --bootstrap-server localhost:9192 --describe --topic products-events`

#### Describe all topics
`./kafka-topics.sh --bootstrap-server localhost:9192 --describe --exclude-internal`

#### Read from the beginning of a topic
`./kafka-console-consumer.sh --bootstrap-server localhost:9192 --topic products-events --from-beginning`

#### Read only new messages
`./kafka-console-consumer.sh --bootstrap-server localhost:9192 --topic products-events`

#### Read with limit (max messages)
`./kafka-console-consumer.sh --bootstrap-server localhost:9192 --topic products-events --from-beginning --max-messages 10`

#### Read with formatting (pretty print JSON)
`./kafka-console-consumer.sh --bootstrap-server localhost:9192 \
--topic products-events --from-beginning \
--formatter-property print.key=true \
--formatter--property print.value=true`