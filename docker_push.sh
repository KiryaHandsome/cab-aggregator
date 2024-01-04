#!/bin/bash

# Set container registry information
REGISTRY_USERNAME="kiryahandsome"
REGISTRY_PASSWORD=$DOCKER_PASSWORD
REGISTRY_NAMESPACE=$REGISTRY_USERNAME

./mvnw clean verify -DskipTests

# Iterate over Maven modules in the current directory
for module in $(find . -maxdepth 1 -type d); do
  if [ -f "$module/Dockerfile" ]; then
    module_name=$(basename "$module") # Extract the module name from the path
    docker build -t "$REGISTRY_NAMESPACE/$module_name:latest" "$module"
    docker login -u "$REGISTRY_USERNAME" -p "$REGISTRY_PASSWORD"
    docker push "$REGISTRY_NAMESPACE/$module_name:latest"
  fi
done

