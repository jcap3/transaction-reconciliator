FROM openjdk:8
WORKDIR /home/jcap/
ADD target/transaction-reconciliator.jar /home/jcap/app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]