FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar appShareItServer.jar
CMD ["java","-jar","/appShareItServer.jar", "--server.port=9090"]