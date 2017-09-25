FROM java:8

RUN apt-get update --fix-missing
RUN apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

VOLUME /tmp

EXPOSE 8080

ADD self-metrics-api*.jar /

CMD java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /self-metrics-api*.jar
