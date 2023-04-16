FROM eclipse-temurin:17
RUN mkdir /opt/app
RUN apt-get update \
    && apt-get install wget \
    && apt-get install -y netcat \
    && wget http://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh \
    && chmod +x ./wait-for-it.sh
COPY target/restpost-0.0.1-SNAPSHOT.jar opt/app/app.jar
CMD ["java", "-jar","/opt/app/app.jar"]

