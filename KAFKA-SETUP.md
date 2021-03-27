# Kafka local setup
Run Kafka and Zookeeper using Docker Compose/Kubernetes.  
You can choose between:
- `docker-compose.yml`
- `docker-compose-confluent-platform.yml`
- `kubernetes.yml`
    - docker-compose up -d
    - docker-compose -f docker-compose-cluster.yml up -d
// todo: add Kubernetes description

## Confluent Kafka Platform
The Confluent setup provides web UI for the whole Kafka platform.  
More information you can find in this guideline: <https://docs.confluent.io/platform/current/quickstart/ce-docker-quickstart.html>

## Docker and Docker Compose commands
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
