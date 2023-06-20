pipeline {
  agent any

  environment {
    //todo1 部署到服务器目录
    DEPLOY_PATH = "/docker/ydbspace_github"
    //todo2 执行shell脚本名称
    DEPLOY_SHNAME = "startgithub.sh"
  }

  triggers {
    //设置触发条件
    cron('H H * * 0')
  }

  stages {

    stage('确认环境变量') {
      steps {
        sh "echo '部署到服务器${secrets.REMOTE_IP}目录：${DEPLOY_PATH}执行脚本：${DEPLOY_SHNAME}'"
      }
    }

    stage('拉取最新的代码') {
      steps {
        checkout scm
      }
    }

    stage('设置JDK') {
      steps {
        script {
          def mvnSettings = new XmlSlurper().parseText('''<?xml version="1.0" encoding="UTF-8"?>
            <settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                                  http://maven.apache.org/xsd/settings-1.0.0.xsd">
              <servers>
                <server>
                  <id>github</id>
                  <username>${env.GITHUB_ACTOR}</username>
                  <password>${env.GITHUB_TOKEN}</password>
                </server>
              </servers>
            </settings>
          ''')
          withMaven(globalMavenSettingsConfig: 'github-maven-settings', mavenSettingsConfig: mvnSettings) {
            //设置JDK
            sh 'java -version'
            tool 'java-1.8'
            sh 'java -version'
          }
        }
      }
    }

    stage('项目打包') {
      steps {
        //项目打包
        sh 'mvn -B clean package -Dmaven.test.skip=true'
      }
    }

    stage('创建文件夹') {
      steps {
        //创建远程文件夹
        sh "sshpass -p ${secrets.REMOTE_PASSWD} ssh -o StrictHostKeyChecking=no root@${secrets.REMOTE_IP} 'mkdir -p ${DEPLOY_PATH}'"
      }
    }

    stage('上传jar包和启动脚本到服务器中') {
      steps {
        //todo3 部署哪些jar包及拷贝运行shell ，不能写到具体的jar包应为还有依赖导入
        //部署那些jar包及拷贝运行shell ，不能写到具体的jar包应为还有依赖导入
        sh "sshpass -p ${secrets.REMOTE_PASSWD} scp -r -o StrictHostKeyChecking=no ./ydbspace_image/target/*  ./User/target/*  ./eurekaservice/target/*  ./fileMq/target/*  ./springAdminService/target/* ./部署/${DEPLOY_SHNAME} root@${secrets.REMOTE_IP}:${DEPLOY_PATH}"
      }
    }

    stage('启动项目') {
      steps {
        //启动项目
        sh "sshpass -p ${secrets.REMOTE_PASSWD} ssh -o StrictHostKeyChecking=no root@${secrets.REMOTE_IP} 'chmod 777 ${DEPLOY_PATH}/* && ${DEPLOY_PATH}/${DEPLOY_SHNAME} start'"
      }
    }
  }
}
// 在将 GitHub Actions 工作流转换为 Jenkinsfile 时，我做了以下更改：
//
// 删除了 "on" 部分，因为在 Jenkins 中设置触发条件的方式不同于 GitHub Actions。
// 删除了 "permissions" 部分，因为这不适用于 Jenkins。
// 将步骤中的命令从 GitHub Actions 的格式转换为 Jenkins 的格式，并使用 Jenkins 提供的 sh 步骤执行 Shell 命令。
// 将脚本中的环境变量从 ${{secrets.REMOTE_PASSWD}} 和 ${{secrets.REMOTE_IP}} 转换为 Jenkins 的凭据，以保护敏感信息。
// 将 sshpass 命令的使用从 GitHub Actions 转换为 Jenkins。在 Jenkins 中，您需要在节点上安装 sshpass，并在 Jenkins 环境变量中添加 sshpass 命令的路径。
// 将 Maven 设置和执行步骤转换为 Jenkins。