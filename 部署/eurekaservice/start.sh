# 功能：部署脚本会在部署主机组的每台机器上执行
# 使用场景：先将制品包解压缩到指定目录中，再执行启动脚本deploy.sh，脚本示例地址：https://gitee.com/gitee-go/spring-boot-maven-deploy-case/blob/master/deploy.sh
# mkdir -p /home/admin/app
# tar zxvf ~/gitee_go/deploy/output.tar.gz -C /home/admin/app
# sh /home/admin/app/deploy.sh restart
# 如果你是php之类的无需制品包的制品方式，可以使用 git clone 或者 git pull 将源代码更新到服务器，再执行其他命令
# git clone ***@***.git
mkdir -p /docker/部署
tar zxvf ~/gitee_go/deploy/output.tar.gz -C /docker/部署
##关闭之前jar包

#   jar包名字
    jarname=( )
# 功能：部署脚本会在部署主机组的每台机器上执行
# 使用场景：先将制品包解压缩到指定目录中，再执行启动脚本deploy.sh，脚本示例地址：https://gitee.com/gitee-go/spring-boot-maven-deploy-case/blob/master/deploy.sh
# mkdir -p /home/admin/app
# tar zxvf ~/gitee_go/deploy/output.tar.gz -C /home/admin/app
# sh /home/admin/app/deploy.sh restart
# 如果你是php之类的无需制品包的制品方式，可以使用 git clone 或者 git pull 将源代码更新到服务器，再执行其他命令
# git clone ***@***.git
mkdir -p /docker/部署
#打得包解压
tar zxvf ~/gitee_go/deploy/output.tar.gz -C /docker/部署
##关闭之前jar包

#   jar包名字
    jarname=( )
jarname[0]=eurekaservice.jar
jarname[1]=ydbspace_image.jar

    echo "循环停止jar包开始"
    cd /docker/部署
    for(( i=0;i<${#jarname[@]};i++)) do
    #${#jarname[@]}获取数组长度用于循环
    echo ${jarname[i]};

#    停止jar包
    APP_NAME=${jarname[i]}

    tpid=`ps -ef|grep $APP_NAME|grep -v grep|grep -v kill|awk '{print $2}'`
    if [ ${tpid} ]; then
        echo 'Stop Process...'  $APP_NAME
        kill -15 $tpid
    fi
    sleep 5
    tpid=`ps -ef|grep $APP_NAME|grep -v grep|grep -v kill|awk '{print $2}'`
    if [ ${tpid} ]; then
        echo 'Kill Process!'
        kill -9 $tpid
    else
    echo "启动 jar包"
    nohup  java -jar   $APP_NAME >$APP_NAME.log &
    done;
