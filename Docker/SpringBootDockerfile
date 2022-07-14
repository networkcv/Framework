FROM jdk:8
MAINTAINER Docker springboot 
# TODO  /bin/sh:  echo: not found
# # 修改源 
# RUN echo "http://mirrors.aliyun.com/alpine/latest-stable/main/" > /etc/apk/repositories && \ echo "http://mirrors.aliyun.com/alpine/latest-stable/community/" >> /etc/apk/repositories

# # 安装需要的软件，解决时区问题 
# RUN apk --update add curl bash tzdata && \ rm -rf /var/cache/apk/*

# #修改镜像为东八区时间 
# ENV TZ Asia/Shanghai 
ARG JAR_FILE 
COPY ${JAR_FILE} app.jar 
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]