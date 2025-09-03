

pipeline {
    agent any
    environment {
        CI = 'true'
    }
    stages {
        stage('checkout') {
            steps {
              checkout scmGit(branches: [[name: "${env.BRANCH_NAME}"]], browser: github('https://github.com/dronDrew/lab_03'), extensions: [localBranch("${env.BRANCH_NAME}")],  userRemoteConfigs: [[credentialsId: 'PAT',url: 'https://github.com/dronDrew/lab_03']])
            }
        }
        stage('build') {
            steps {
                sh 'chmod +x ./scripts/build.sh'
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
               echo "build image"
            }
        }
        stage('deploy') {
            steps {
                echo 'deploy'
            }
        }
    }
}


