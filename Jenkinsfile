

pipeline {
    agent any
    environment {
        CI = 'true'
    }
    stages {
        stage('checkout') {
            steps {
              checkout scmGit(branches: [[name: '${env.BRANCH_NAME}']], browser: github('https://github.com/dronDrew/lab_03'), extensions: [], userRemoteConfigs: [[url: 'https://github.com/dronDrew/lab_03']])
            }
        }
        stage('build') {
            steps {
                sh './scripts/build.sh'
            }
        }
        stage('test') {
            steps {
                sh './scripts/test.sh'
            }
        }
        stage('build docker image') {
            steps {
               def customImage = docker.build("my-image:${env.BUILD_ID}")
            }
        }
        stage('deploy') {
            steps {
                echo 'deploy'
            }
        }
    }
}


