FROM java:jre-alpine

RUN apk add -U --no-cache  bash

ENV ZOOKEPER_VERSION "3.4.10"
RUN wget -O zookeeper.tar.gz "http://apache.mirror.anlx.net/zookeeper/stable/zookeeper-${ZOOKEPER_VERSION}.tar.gz" && \
    tar  -xf zookeeper.tar.gz -C /root && \
    rm -f zookeeper.tar.gz
ENV PATH "/root/zookeeper-${ZOOKEPER_VERSION}/bin:$PATH"
ADD zoo.cfg /root/zookeeper-${ZOOKEPER_VERSION}/conf/zoo.cfg
EXPOSE 2181
CMD ["zkServer.sh", "start-foreground"]
