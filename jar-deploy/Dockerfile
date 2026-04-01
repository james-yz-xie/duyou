# 使用 JRE 8 运行
FROM alpine:3.13

# 选用国内镜像源以提高下载速度
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.tencent.com/g' /etc/apk/repositories \
    && apk add --update --no-cache openjdk8-jre-base \
    && rm -f /var/cache/apk/*

# 设置时区为上海
RUN apk add tzdata && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo Asia/Shanghai > /etc/timezone

# 安装证书
RUN apk add ca-certificates

WORKDIR /app

# 复制预编译的 jar 包
COPY app.jar /app/app.jar

# 暴露端口
EXPOSE 80

# 启动命令
CMD ["java", "-jar", "/app/app.jar"]