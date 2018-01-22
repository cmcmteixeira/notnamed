FROM java:jre-alpine
ENV  KAFKA_VERSION "2.11-1.0.0"
RUN mkdir /opt
RUN apk add -U --no-cache  bash
RUN wget -O kafka.tgz "http://apache.mirror.anlx.net/kafka/1.0.0/kafka_${KAFKA_VERSION}.tgz" && \
    tar -xzf kafka.tgz -C /root && \
    mv "/root/kafka_${KAFKA_VERSION}" "/root/kafka" && \
    rm -f kafka.tgz
ENV PATH "/root/kafka/bin:$PATH"
ADD server.properties "/root/kafka/config/server.properties"
CMD ["kafka-server-start.sh", "/root/kafka/config/server.properties"]
