FROM openjdk:8-alpine
MAINTAINER philipp.dangl@gmail.com
COPY ./target/mail-client-1.0.0.jar .
#setting /home/user/hello as our work directory. Means code will reside here
#WORKDIR /home/user/hello
CMD ["java", "-jar", "mail-client-1.0.0.jar"]
EXPOSE 8080