FROM java:jre-alpine
ENV  KAFKA_VERSION "2.11-1.0.0"
RUN mkdir /opt
RUN apk add -U --no-cache  bash
RUN wget -O kafka.tgz "http://apache.mirror.anlx.net/kafka/1.0.0/kafka_${KAFKA_VERSION}.tgz" && \
    tar -xzf kafka.tgz -C /root && \
    rm -f kafka.tgz
ENV PATH "/root/kafka_$KAFKA_VERSION/bin:$PATH"
ADD server.properties "/root/kafka_${KAFKA_VERSION}/config/server.properties"
CMD ["kafka-server-start.sh", "/root/kafka_${KAFKA_VERSION}/config/server.properties"]
