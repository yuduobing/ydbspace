// Git credentials ID
def git_auth = "1e271c92-164d-4a9c-8ea7-a38c8ba1741d"
// Git URL
def git_url = "https://gitee.com/ydb6/wp.git"
//部署目录
def DEPLOY_PATH = "/home/ydbspce"
def DEPLOY_SHNAME = "start_jenkins.sh"
//脚本蜜女
node {

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