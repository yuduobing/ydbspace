#!/bin/bash

# 设置jar包位置
DEPLOY_PATH=/home/ydbspce
mkdir -p "${DEPLOY_PATH}"
mkdir -p "${DEPLOY_PATH}/logs"
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
# 启用错误检查功能
set -e
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
	sleep 3
	tpid=$(ps -ef | grep "${APP_NAME}" | grep -v grep | grep -v kill | awk '{print $2}')
	if [ "${tpid}" ]; then
		echo '杀死进程'
		kill -9 "${tpid}"
	fi

	# Xms — 堆内存初始大小
	# Xmx — 堆内存最大值
	# MetaspaceSize — 永久内存初始大小
	# MaxMetaspaceSize — 永久内存最大值
	#-XX:+NewRatio=4是Java虚拟机的一个参数，用于设置新生代和老年代的比例。具体来说，这个参数表示新生代内存与老年代内存的比例为1:4，也就是新生代占整个堆内存的1/5，老年代占整个堆内存的4/5。
#-XX:+UseParNewGC: 启用并行垃圾回收器 ParNew GC。
#-XX:NewRatio=4: 新生代和老年代的比率为 1:4。
#-XX:SurvivorRatio=8: Eden 空间和 Survivor 空间的比率为 8:1。
#-XX:ParallelGCThreads=12: 并行垃圾回收线程数为 12。
  java_opts="-XX:+UseG1GC  -XX:NewRatio=4 -XX:SurvivorRatio=8   -XX:ParallelGCThreads=12"
	# MaxMetaspaceSize元空间  Xmx堆内存
	if [ "${APP_NAME}" == "ydbspace_image.jar" ]; then
	   echo "启动jar包：  java -jar -Xms1g -Xmx1g -XX:MetaspaceSize=64M  -XX:MaxMetaspaceSize=512M ${java_opts}  ${DEPLOY_PATH}/${APP_NAME} --spring.profiles.active=prod > ./logs/${APP_NAME}.log & "

		nohup java -jar -Xms512M -Xmx512M -XX:MetaspaceSize=64M  -XX:MaxMetaspaceSize=512M  ${java_opts} "${DEPLOY_PATH}/${APP_NAME}" --spring.profiles.active=unraid > "./logs/${APP_NAME}.log" &
   pid2=$!
#     最后一个程序等待执行
     wait $pid2
	elif [ "${APP_NAME}" == "eurekaservice.jar" ]; then
	  echo "启动jar包：  java -jar -Xms64M -Xmx256M -XX:MetaspaceSize=64M -XX:MaxMetaspaceSize=256M  ${DEPLOY_PATH}/${APP_NAME} --spring.profiles.active=prod > ./logs/${APP_NAME}.log & "

		nohup java -jar -Xms256M -Xmx256M -XX:MetaspaceSize=64M -XX:MaxMetaspaceSize=256M "${DEPLOY_PATH}/${APP_NAME}" --spring.profiles.active=unraid > "./logs/${APP_NAME}.log" &
	else
	  echo "启动jar包：  java -jar -Xms64M -Xmx128M -XX:MetaspaceSize=64M -XX:MaxMetaspaceSize=128M  ${DEPLOY_PATH}/${APP_NAME} --spring.profiles.active=prod > ./logs/${APP_NAME}.log & "

		# 指定生产环境包，必须用绝对路径
		nohup java -jar -Xms128M -Xmx128M -XX:MetaspaceSize=64M -XX:MaxMetaspaceSize=128M "${DEPLOY_PATH}/${APP_NAME}" --spring.profiles.active=prod > "./logs/${APP_NAME}.log" &
	fi

	# 等待程序执行
	echo "${APP_NAME}执行结束***------------------------***"

done

# 问题记录
# github action启动shell脚本启动jar包报Error: Could not find or load main class org.springframework.boot.loade
# 用绝对路径启动jar包
# 这里面if判断已修改