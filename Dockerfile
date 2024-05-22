FROM eclipse-temurin

RUN mkdir /usr/src/server
WORKDIR /usr/src/server

COPY . .

RUN javac server.java

CMD ["java", "server"]