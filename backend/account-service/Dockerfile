FROM openjdk:17-alpine
ARG JAR_FILE=target/account-service.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]
# Define port because it is not defined in the application.properties file
EXPOSE 8071
