FROM openjdk:14-alpine
COPY target/products-*.jar products.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "products.jar"]