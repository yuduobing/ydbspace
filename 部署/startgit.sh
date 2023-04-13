##todo1 设置jar包位置
path=//docker/部署
mkdir -p $path

#打得包解压
tar zxvf ~/gitee_go/deploy/output.tar.gz -C /docker/部署
##关闭之前jar包

#   jar包名字
jarname=()
jarname[0]=eurekaservice.jar
jarname[1]=ydbspace_image.jar
jarname[2]=filemq-exec.jar
jarname[3]=User.jar
echo "循环停止jar包开始"
cd /docker/部署/target
for ((i = 0; i < ${#jarname[@]}; i++)); do
  #${#jarname[@]}获取数组长度用于循环
  echo ${jarname[i]}

  #    停止jar包
  APP_NAME=${jarname[i]}
  echo '开始执行' $APP_NAME
  tpid=$(ps -ef | grep $APP_NAME | grep -v grep | grep -v kill | awk '{print $2}')
  if [ ${tpid} ]; then
    echo 'Stop Process...' $APP_NAME
    kill -15 $tpid
  fi
  sleep 5
  tpid=$(ps -ef | grep $APP_NAME | grep -v grep | grep -v kill | awk '{print $2}')
  if [ ${tpid} ]; then
    echo 'Kill Process!'
    kill -9 $tpid
  fi
  echo "启动 jar包"
  #   指定生产环境包
  nohup java -jar $APP_NAME --spring.profiles.active=prod >$APP_NAME.log &

done
