FROM maven:3.8.6-jdk-11
ADD . /home/workdir/
WORKDIR /home/workdir/
RUN mvn install -D maven.test.skip=true

FROM openjdk:11

COPY --from=0 /home/workdir/app/target/imageUI.jar /home/workdir/app.jar
WORKDIR /home/workdir/
CMD ["java", "-jar", "app.jar"]
