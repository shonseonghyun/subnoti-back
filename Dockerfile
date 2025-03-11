#Build Stage
FROM openjdk:17-jdk-alpine AS builder

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew

RUN ./gradlew clean build -x test

#Run Stage
FROM openjdk:17-jdk-alpine

## 환경변수 선언(이는 container를 실행할 때 옵션을 주지 않으면 LOCAL이 default로 설정되도록 선언)
ENV SPRING_PROFILE=LOCAL
ENV SPRING_PORT=8000

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE ${SPRING_PORT}

ENTRYPOINT ["java","-Dspring.profiles.active=${SPRING_PROFILE}","-jar","app.jar"]

