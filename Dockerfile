FROM openjdk:14 AS builder

RUN mkdir -p /root/src/api
WORKDIR /root/src/api

COPY gradle ./gradle
COPY build.gradle gradlew settings.gradle ./
RUN ./gradlew dependencies

COPY . .
RUN ./gradlew build

FROM openjdk:14-jdk-alpine

WORKDIR /root/src/api

COPY --from=builder /root/src/api/build/libs/jodd-all.jar /root/src/api/jodd-all.jar

EXPOSE 1234
EXPOSE 1235

ENTRYPOINT ["java","-jar","jodd-all.jar"]

# docker build -t jodd .
# docker run -d --name jodd -p 1234:1234 jodd:latest
