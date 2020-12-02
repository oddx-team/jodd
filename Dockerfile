# Build jar file
FROM gradle:6.7.1-jdk11 AS builder
WORKDIR /root/src/api

COPY build.gradle settings.gradle ./
RUN gradle --no-daemon --console=plain dependencies

COPY . .
RUN gradle --no-daemon --console=plain build

# Build native image
FROM oracle/graalvm-ce:20.2.0-java11 AS build-aot
WORKDIR /root/src/aot

COPY --from=builder /root/src/api/build/libs/jodd-all.jar /root/src/aot/jodd.jar
COPY --from=builder /root/src/api/native-image.json /root/src/aot/native-image.json
RUN gu install native-image

# BEGIN PRE-REQUISITES FOR STATIC NATIVE IMAGES FOR GRAAL 20.2.0
# SEE: https://github.com/oracle/graal/blob/master/substratevm/StaticImages.md
ARG RESULT_LIB="/staticlibs"

RUN mkdir ${RESULT_LIB} && \
    curl -L -o musl.tar.gz https://musl.libc.org/releases/musl-1.2.1.tar.gz && \
    mkdir musl && tar -xvzf musl.tar.gz -C musl --strip-components 1 && cd musl && \
    ./configure --disable-shared --prefix=${RESULT_LIB} && \
    make && make install && \
    cd / && rm -rf /muscl && rm -f /musl.tar.gz && \
    cp /usr/lib/gcc/x86_64-redhat-linux/4.8.2/libstdc++.a ${RESULT_LIB}/lib/

ENV PATH="$PATH:${RESULT_LIB}/bin"
ENV CC="musl-gcc"

RUN curl -L -o zlib.tar.gz https://zlib.net/zlib-1.2.11.tar.gz && \
   mkdir zlib && tar -xvzf zlib.tar.gz -C zlib --strip-components 1 && cd zlib && \
   ./configure --static --prefix=${RESULT_LIB} && \
    make && make install && \
    cd / && rm -rf /zlib && rm -f /zlib.tar.gz
#END PRE-REQUISITES FOR STATIC NATIVE IMAGES FOR GRAAL 20.2.0

RUN native-image --static --no-fallback --libc=musl \
    --no-server --enable-http --enable-https --enable-all-security-services --enable-url-protocols=http,https \
    -H:ReflectionConfigurationFiles=native-image.json --allow-incomplete-classpath -jar ./jodd.jar jodd

# Run binary file in alpine image
FROM amd64/alpine:latest
# RUN apk add ca-certificates rsync openssh
WORKDIR /root/src/binary

COPY --from=build-aot /root/src/aot/jodd /root/src/binary/jodd

EXPOSE 1234
EXPOSE 1235

ENTRYPOINT ["./jodd"]

# docker build -t jodd .
# docker run -d --name jodd -p 1234:1234 jodd:latest
