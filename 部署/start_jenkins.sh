#!/bin/bash

# 设置jar包位置
DEPLOY_PATH=/home/ydbspce
mkdir -p "${DEPLOY_PATH}"

# jar包名字
jar_name=(
  "springAdminService.jar"
  "eurekaservice.jar"
  "fileMq-exec.jar"
  "User.jar"
  "ydbspace_image.jar"
)

cd "${DEPLOY_PATH}"
pwd
echo "jenkinsjar包开始执行啦。噜啦啦噜啦啦"

for ((i = 0; i < ${#jar_name[@]}; i++)); do
	# ${#jar_name[@]}获取数组长度用于循环

	# 停止jar包
	APP_NAME="${jar_name[i]}"
	echo "${APP_NAME}开始执行***------------------------***"
	tpid=$(ps -ef | grep "${APP_NAME}" | grep -v grep | grep -v kill | awk '{print $2}')
	if [ "${tpid}" ]; then
		echo "停止进程 ${APP_NAME}"
		kill -15 "${tpid}"
	fi
	sleep 2
	tpid=$(ps -ef | grep "${APP_NAME}" | grep -v grep | grep -v kill | awk '{print $2}')
	if [ "${tpid}" ]; then
		echo '杀死进程'
		kill -9 "${tpid}"
	fi

	# Xms — 堆内存初始大小
	# Xmx — 堆内存最大值
	# MetaspaceSize — 永久内存初始大小
	# MaxMetaspaceSize — 永久内存最大值
	echo "启动jar包：  java -jar -Xms64M -Xmx128M -XX:MetaspaceSize=64M -XX:MaxMetaspaceSize=128M  ${DEPLOY_PATH}/${APP_NAME} --spring.profiles.active=prod > ${APP_NAME}.log & "

	# MaxMetaspaceSize元空间  Xmx堆内存
	if [ "${APP_NAME}" == "ydbspace_image.jar" ]; then
		nohup java -jar -Xms128M -Xmx512M -XX:MetaspaceSize=128M -XX:MaxMetaspaceSize=512M "${DEPLOY_PATH}/${APP_NAME}" --spring.profiles.active=unraid > "${APP_NAME}.log" &
	elif [ "${APP_NAME}" == "eurekaservice.jar" ]; then
		nohup java -jar -Xms64M -Xmx256M -XX:MetaspaceSize=64M -XX:MaxMetaspaceSize=256M "${DEPLOY_PATH}/${APP_NAME}" --spring.profiles.active=unraid > "${APP_NAME}.log" &
	else
		# 指定生产环境包，必须用绝对路径
		nohup java -jar -Xms64M -Xmx128M -XX:MetaspaceSize=64M -XX:MaxMetaspaceSize=128M "${DEPLOY_PATH}/${APP_NAME}" --spring.profiles.active=prod > "${APP_NAME}.log" &
	fi

	# 等待程序执行
	echo "${APP_NAME}执行结束***------------------------***"
done

# 问题记录
# github action启动shell脚本启动jar包报Error: Could not find or load main class org.springframework.boot.loade
# 用绝对路径启动jar包
# 这里面if判断已修改