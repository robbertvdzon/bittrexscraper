FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD build/libs/bittrexscraperall-*.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS \
-Djava.security.egd=file:/dev/./urandom \
-Dencrypt.key=$encryptkey \
-Dspring.profiles.active=$springprofilesactive \
-Dspring.cloud.config.uri=$springcloudconfiguri \
-jar /app.jar" ]


