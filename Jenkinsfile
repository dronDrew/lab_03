

pipeline {
    agent any
    triggers {
    	pollSCM('')
    }
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
                script {
                    def containerName = "node${CUR_BRANCH}"
                    def containerStatus = sh(script: "docker ps -a --filter name=^/${containerName}\$ --format '{{.Names}}'", returnStdout: true).trim()
                    if (containerStatus == containerName)
                    {
                       sh(script: "docker container stop ${containerName}")
                    }
                    def contExist = sh(script: "docker ps --filter name=^/${containerName}\$ --format '{{.Names}}'", returnStdout: true).trim()
                    if (contExist == containerName)
                    {
                      sh(script: "docker container rm ${containerName}")
                    }
                    if (CUR_BRANCH == main) {
                        sh "docker run -d --name ${containerName} -p 3000:3000 node${CUR_BRANCH}:${TAG}"
                    } else {
                        sh "docker run -d --name ${containerName} -p 3001:3000 node${CUR_BRANCH}:${TAG}"
                    }
                }
            }
        }
    }
}


