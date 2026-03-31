# 构建阶段 - 使用 JDK 8 编译
FROM maven:3.6.0-jdk-8-slim as build

WORKDIR /app

# 复制源代码和配置文件
COPY src /app/src
COPY settings.xml pom.xml /app/

# 编译打包
RUN mvn -s /app/settings.xml -f /app/pom.xml clean package -DskipTests

# 运行阶段 - 使用 JRE 8
FROM openjdk:8-jre-alpine

# 设置时区为上海
RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo Asia/Shanghai > /etc/timezone && \
    apk del tzdata

# 安装 ca-certificates（微信云托管需要）
RUN apk add --no-cache ca-certificates

WORKDIR /app

# 复制构建产物
COPY --from=build /app/target/*.jar /app/app.jar

# 暴露端口（微信云托管默认80）
EXPOSE 80

# 启动命令
CMD ["java", "-jar", "/app/app.jar"]
