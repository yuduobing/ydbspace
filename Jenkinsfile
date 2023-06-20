// Git credentials ID
def git_auth = "1e271c92-164d-4a9c-8ea7-a38c8ba1741d"
// Git URL
def git_url = "https://gitee.com/kekesam/spring-docker-demo.git"

node {
    environment {
    //todo1 部署到服务器目录
    DEPLOY_PATH = "/docker/ydbspace_github"
    //todo2 执行shell脚本名称
    DEPLOY_SHNAME = "startgithub.sh"
    }
  stage('Fetch code') {
    checkout([$class: 'GitSCM', branches: [[name: "*/${branch}"]], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: "${git_auth}", url: "${git_url}"]]])
  }

  stage('Compile and install common entity bean') {
    sh "mvn clean install -Dmaven.test.skip=true"
  }

  stage('项目打包') {
    sh "mvn -B clean package -Dmaven.test.skip=true"
  }
 stage('创建文件夹') {
    sh "mkdir -p ${DEPLOY_PATH}"
  }
   stage('上传项目') {
      sh "cp -r  ./ydbspace_image/target/*  ./User/target/*  ./eurekaservice/target/*  ./fileMq/target/*  ./springAdminService/target/* ./部署/${DEPLOY_SHNAME}  ${DEPLOY_PATH} "
    }
  stage('启动项目') {
 sh "chmod 777 ${DEPLOY_PATH}/*  &&  ${DEPLOY_PATH}/${DEPLOY_SHNAME} start"

  }
}