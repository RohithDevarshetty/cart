FROM maven:3.8.7-eclipse-temurin-19 AS build
WORKDIR /cart
COPY pom.xml .
COPY src /cart/src
RUN mvn clean package -DskipTests

FROM maven:3.8.7-eclipse-temurin-19
WORKDIR /cart
COPY --from=build /cart/target/cart-0.0.1-SNAPSHOT.jar /cart/cart-0.0.1-SNAPSHOT.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/cart/cart-0.0.1-SNAPSHOT.jar"]
