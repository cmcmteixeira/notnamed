FROM java:openjdk-8-jre-alpine
ENV SBT_VERSION 1.0.0
ENV SBT_HOME /usr/local/sbt-launcher-packaging-${SBT_VERSION}
RUN apk add --no-cache curl tar bash
RUN curl -L "https://github.com/sbt/sbt/releases/download/v$SBT_VERSION/sbt-$SBT_VERSION.tgz" | tar -xz -C /root && \
    ln -s /root/sbt/bin/sbt /usr/local/bin/sbt && \
    chmod 0755 /usr/local/bin/sbt
RUN mkdir /app
RUN mkdir /app/groups
RUN mkdir /app/commons
WORKDIR /app/groups
