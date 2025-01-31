#!/bin/bash

# List of services and their corresponding expected ports (you can add more if needed)
declare -A services
services=(
    ["zookeeper"]="2181"
    ["broker"]="9092"
    ["schema-registry"]="8081"
    ["connect"]="8083"
    ["control-center"]="9021"
    ["ksqldb-server"]="8088"
    ["ksqldb-cli"]="8088"
    ["ksql-datagen"]="9092"
    ["rest-proxy"]="8082"
)

# Function to check if a service is up by checking the container status
check_service_status() {
    local service_name=$1
    local port=$2

    echo "Checking if $service_name is up on port $port..."

    # Check if the service container is running
    if docker ps --filter "name=$service_name" --filter "status=running" | grep -q "$service_name"; then
        echo "$service_name is running."
    else
        echo "$service_name is NOT running."
        return 1
    fi

    # Check if the port is accessible
#    todo: check how to access a service on GitLab
#    if nc -zv $service_name $port 2>/dev/null; then
#        echo "$service_name is accessible on port $port."
#    else
#        echo "$service_name is NOT accessible on port $port"
#        return 1
#    fi
}

# Function to wait and retry checking services
wait_for_services() {
    local timeout=60
    local interval=5
    local elapsed=0

    while [ $elapsed -lt $timeout ]; do
        all_services_up=true

        # Loop through each service and check its status
        for service in "${!services[@]}"; do
            check_service_status "$service" "${services[$service]}"
            if [ $? -ne 0 ]; then
                all_services_up=false
            fi
        done

        if [ "$all_services_up" = true ]; then
            echo "All services are up and running successfully!"
            return 0
        fi

        echo "Retrying in $interval seconds. Elapsed: $elapsed"
        sleep $interval
        elapsed=$((elapsed + interval))
    done

    echo "ERROR: Timeout reached. Not all services are up after $timeout seconds."
    return 1
}

# Run the service check with retries
wait_for_services
