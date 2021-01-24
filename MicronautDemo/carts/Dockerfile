FROM openjdk:14-alpine
COPY target/carts-*.jar carts.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "carts.jar"]