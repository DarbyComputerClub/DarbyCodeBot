FROM java:latest
COPY . /usr/src/darbycodebot
WORKDIR /usr/src/darbycodebot
CMD ["./gradlew", "run"]
