# ================= STAGE 1: Build =================
# Gradle 이미지를 사용하여 프로젝트를 빌드합니다.
FROM gradle:8.5.0-jdk17 AS build

# 작업 디렉터리를 /home/gradle/project로 설정합니다.
WORKDIR /home/gradle/project

# 소스 코드를 복사합니다.
COPY . .

# Gradle을 사용하여 실행 권한을 부여하고 프로젝트를 빌드합니다.
RUN chmod +x ./gradlew
RUN ./gradlew bootJar

# ================= STAGE 2: Run =================
# 더 가벼운 OpenJDK 17 이미지를 기반으로 최종 이미지를 만듭니다.
FROM openjdk:17-jdk-slim

# 작업 디렉터리를 /app으로 설정합니다.
WORKDIR /app

# 빌드 단계(build)에서 생성된 .jar 파일을 /app/app.jar로 복사합니다.
COPY --from=build /home/gradle/project/build/libs/*.jar app.jar

# 8080 포트를 외부에 노출합니다.
EXPOSE 8080

# 컨테이너가 시작될 때 애플리케이션을 실행합니다.
ENTRYPOINT ["java", "-jar", "app.jar"]