FROM openjdk:8-jre-alpine
ADD build/libs/authorization.jar /app/authorization.jar
EXPOSE 10030
CMD java -jar /app/authorization.jar --connection=cont_postgresql