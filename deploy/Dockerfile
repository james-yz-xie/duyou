# 使用 JRE 8 运行
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
COPY app.jar /app/app.jar

# 暴露端口（微信云托管默认80）
EXPOSE 80

# 启动命令
CMD ["java", "-jar", "/app/app.jar"]