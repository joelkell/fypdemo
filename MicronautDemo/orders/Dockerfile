FROM openjdk:14-alpine
COPY target/orders-*.jar orders.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "orders.jar"]