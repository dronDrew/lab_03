

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
        stage('Checkout') {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: "*/${env.BRANCH_NAME}"]],
                    doGenerateSubmoduleConfigurations: false, // Corrected typo
                    extensions: [],
                    userRemoteConfigs: [[
                        credentialsId: 'PAT',
                        url: 'https://github.com/dronDrew/lab_03'
                    ]]
                ])
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
        stage('push to docker hub') {
            steps {
                script {
			withDockerRegistry(credentialsId: 'DOCKER_HUB', url: 'https://app.docker.com/') {
			sh "docker push node${CUR_BRANCH}:${TAG}"
			}
                }
            }
        }
        stage('deploy') {
            steps {
                script {
                        def containerName = "node${CUR_BRANCH}"
                        def containerStatus = sh(script: "docker ps -a --filter name=^/${containerName}\$ --format '{{.Names}}'", returnStdout: true).trim()
                        
                        if (containerStatus == containerName) {
                            echo "Container '${containerName}' exists. Stopping and removing it."
                            sh "docker container stop ${containerName} || true"
                            sh "docker container rm -f ${containerName}"
                        } else {
                            echo "Container '${containerName}' does not exist."
                        }
                        
                        if (CUR_BRANCH == 'main') {
                            withDockerRegistry(credentialsId: 'DOCKER_HUB', url: 'https://app.docker.com/') {
			sh "docker run -d --name ${containerName} -p 3000:3000 node${CUR_BRANCH}:${TAG}"}
                            
                        } else {
                             withDockerRegistry(credentialsId: 'DOCKER_HUB', url: 'https://app.docker.com/') {
			sh "docker run -d --name ${containerName} -p 3001:3000 node${CUR_BRANCH}:${TAG}"}
                        }
                }
            }
        }
    }
}


