# 736762908522.dkr.ecr.us-east-1.amazonaws.com/zombies-front

FROM node:10-alpine
MAINTAINER Serhii Maksymchuk <serg.maximchuk@gmail.com>

#RUN npm install -g http-server
#CMD ["http-server", "/build", "-p", "8080"]

RUN npm install -g http-server-spa
CMD ["http-server-spa", "/dist", "index.html", "8080"]

HEALTHCHECK --interval=10s --timeout=3s CMD curl -f http://localhost:8080 || exit 1
ENTRYPOINT ["docker-entrypoint.sh"]

EXPOSE 8080

ADD ./dist /dist
