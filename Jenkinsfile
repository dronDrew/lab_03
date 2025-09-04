

pipeline {
    agent any
    environment {
        CUR_BRANCH = "${env.BRANCH_NAME}"
        TAG    = "v1.0"
    }
    stages {
        stage('checkout') {
            steps {
              checkout scmGit(branches: [[name: "${env.BRANCH_NAME}"]], browser: github('https://github.com/dronDrew/lab_03'), extensions: [localBranch("${env.BRANCH_NAME}")],  userRemoteConfigs: [[credentialsId: 'PAT',url: 'https://github.com/dronDrew/lab_03']])
            }
        }
       
        stage('build') {
            steps {
              nodejs(cacheLocationStrategy: workspace(), nodeJSInstallationName: 'node') {
    		sh 'node --version'
                sh 'chmod +x ./scripts/build.sh'
                sh './scripts/build.sh'
		}
            }
        }
        stage('test') {
            steps {
                nodejs(cacheLocationStrategy: workspace(), nodeJSInstallationName: 'node') {
    		  sh 'node --version'
                  sh 'chmod +x ./scripts/build.sh'
                  sh './scripts/test.sh'
		  }
            	} 
            }
        stage('build docker image') {
            steps {
               sh 'chmod +x ./Dockerfile'
               sh "docker build -t node${CUR_BRANCH}:${TAG} ."
               sh "docker image ls"
            }
        }
        stage('deploy') {
            steps {
                echo 'deploy'
            }
        }
    }
}


