FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/discovery-service-1.0-plain.jar DiscoveryService.jar
ENTRYPOINT ["java", "-jar", "DiscoveryService.jar"]

# docker build --tag devhtak/discovery-service:1.0 .
# docker run -d -p 8761:8761 --network ecommerce-network -e "spring.cloud.config.uri=http://config-service:8888" \
# --name discovery-service devhtak/discovery-service:1.0