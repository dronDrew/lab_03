

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
			withDockerRegistry(credentialsId: 'DOCKER_HUB', url: '') {
			sh "docker tag node${CUR_BRANCH}:${TAG} andrdud/node${CUR_BRANCH}:${TAG}"
			sh "docker push andrdud/node${CUR_BRANCH}:${TAG}"
			}
                }
            }
        }
        stage('deploy') {
            steps {
                script {
                        if (CUR_BRANCH == 'main') {
                           build job: 'Deploy_to_main', wait: false
                            
                        } else {
                            build job: 'Deploy_to_dev', wait: false 
                        }
                }
            }
        }
    }
}


