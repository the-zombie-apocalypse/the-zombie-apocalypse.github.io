# 736762908522.dkr.ecr.us-east-1.amazonaws.com/zombies

FROM openjdk:11-jdk-slim
MAINTAINER Serhii Maksymchuk <serg.maximchuk@gmail.com>

RUN apt-get update -y
RUN apt-get install curl -y

HEALTHCHECK --interval=10s --timeout=3s CMD curl -f http://localhost:8000/actuator/health || exit 1

CMD ["java", "-Xmx1g", "-jar", "/app/zombies-app.jar"]

EXPOSE 8000

ADD ./build/libs/zombies-app.jar /app/
