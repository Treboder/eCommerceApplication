FROM openjdk:8-jdk-alpine
MAINTAINER Treboder
COPY target/eCommerceApplication-0.0.1-SNAPSHOT.war eCommerceApplication-0.0.1-SNAPSHOT.war
ENTRYPOINT ["java","-jar","/eCommerceApplication-0.0.1-SNAPSHOT.war"]
EXPOSE 8080