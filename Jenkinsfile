pipeline {
  agent any
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
        sh 'mvn -B clean package -Dmaven.test.skip=true'
      }
    }

    stage('创建文件夹') {
      steps {
        sh "sshpass -p ${secrets.REMOTE_PASSWD} ssh -o StrictHostKeyChecking=no root@${secrets.REMOTE_IP} 'mkdir -p ${DEPLOY_PATH}'"
      }
    }

    stage('上传jar包和启动脚本到服务器中') {
      steps {
        sh "sshpass -p ${secrets.REMOTE_PASSWD} scp -r -o StrictHostKeyChecking=no ./ydbspace_image/target/*  ./User/target/*  ./eurekaservice/target/*  ./fileMq/target/*  ./springAdminService/target/* ./部署/${DEPLOY_SHNAME} root@${secrets.REMOTE_IP}:${DEPLOY_PATH}"
      }
    }

    stage('启动项目') {
      steps {
        sh "sshpass -p ${secrets.REMOTE_PASSWD} ssh -o StrictHostKeyChecking=no root@${secrets.REMOTE_IP} 'chmod 777 ${DEPLOY_PATH}/* && ${DEPLOY_PATH}/${DEPLOY_SHNAME} start'"
      }
    }

  }
  environment {
    DEPLOY_PATH = '/docker/ydbspace_github'
    DEPLOY_SHNAME = 'startgithub.sh'
  }
  triggers {
    cron('H H * * 0')
  }
}