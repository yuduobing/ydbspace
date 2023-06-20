pipeline {
  agent any
  stages {
    stage('Confirm environment variables') {
      steps {
        sh "echo 'Deploying to server ${secrets.REMOTE_IP} directory: ${DEPLOY_PATH} executing script: ${DEPLOY_SHNAME}'"
      }
    }

    stage('Fetch latest code') {
      steps {
        checkout scm
      }
    }

    stage('Set JDK') {
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
            //Set the JDK version
            sh 'java -version'
            tool 'java-1.8'
            sh 'java -version'
          }
        }

      }
    }

    stage('Package the project') {
      steps {
        sh 'mvn -B clean package -Dmaven.test.skip=true'
      }
    }

    stage('Create directory') {
      steps {
        sh "sshpass -p ${secrets.REMOTE_PASSWD} ssh -o StrictHostKeyChecking=no root@${secrets.REMOTE_IP} 'mkdir -p ${DEPLOY_PATH}'"
      }
    }

    stage('Upload JAR files and startup script to the server') {
      steps {
        sh "sshpass -p ${secrets.REMOTE_PASSWD} scp -r -o StrictHostKeyChecking=no ./ydbspace_image/target/*  ./User/target/*  ./eurekaservice/target/*  ./fileMq/target/*  ./springAdminService/target/* ./部署/${DEPLOY_SHNAME} root@${secrets.REMOTE_IP}:${DEPLOY_PATH}"
      }
    }

    stage('Start the project') {
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