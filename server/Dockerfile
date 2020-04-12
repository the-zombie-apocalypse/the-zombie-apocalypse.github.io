# 736762908522.dkr.ecr.us-east-1.amazonaws.com/zombies

FROM openjdk:11-jdk-slim
MAINTAINER Serhii Maksymchuk <serg.maximchuk@gmail.com>

ADD ./build/libs/zombies-app.jar /app/

RUN apt-get update -y
RUN apt-get install curl -y

CMD ["java", "-Xmx1g", "-jar", "/app/zombies-app.jar"]

HEALTHCHECK --interval=10s --timeout=3s CMD curl -f http://localhost:8000/actuator/health || exit 1

EXPOSE 8000

