@Library('lab_lib@dev') _
pipeline {
    agent any
    triggers {
        pollSCM('H/45 * * * *')
    }
    environment {
        CUR_BRANCH = "${env.BRANCH_NAME}"
        TAG = "v1.0"
    }
    stages {
        stage('Checkout') {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: "*/${env.BRANCH_NAME}"]],
                    doGenerateSubmoduleConfigurations: false,
                    extensions: [],
                    userRemoteConfigs: [[
                        credentialsId: 'PAT',
                        url: 'https://github.com/dronDrew/lab_03'
                    ]]
                ])
            }
        }
        stage('Hadolint') {
    agent {
        docker {
            image 'hadolint/hadolint:latest-debian'
        }
    }
    steps {
        sh 'hadolint ./Dockerfile | tee -a hadolint.txt'
    }
    post {
        always {
            archiveArtifacts 'hadolint.txt'
        }
    }
}
        stage('Build') {
            steps {
                nodejs(nodeJSInstallationName: 'node') {
                    sh 'node --version'
                    sh 'chmod +x ./scripts/build.sh'
                    sh './scripts/build.sh'
                }
            }
        }
        stage('Test') {
            steps {
                nodejs(nodeJSInstallationName: 'node') {
                    sh 'node --version'
                    sh 'chmod +x ./scripts/test.sh'
                    sh './scripts/test.sh'
                }
            }
        }
        stage('Build Docker Image') {
            agent {
                docker {
                    image 'docker:24.0'
                    args '--v /var/run/docker.sock:/var/run/docker.sock'
                }
            }
            steps {
                sh "docker build -t node${CUR_BRANCH}:${TAG} ."
            }
        }
        stage('Trivy Docker Image Analyze') {
            steps {
                sh "docker run --rm -v /var/run/docker.sock:/var/run/docker.sock -v /var/jenkins_home/.trivy-cache:/root/.cache aquasec/trivy image --severity HIGH,CRITICAL node${CUR_BRANCH}:${TAG}"
            }
        }
        stage('Push to Docker Hub') {
            steps {
                script {
                    withDockerRegistry(credentialsId: 'DOCKER_HUB', url: '') {
                        sh "docker tag node${CUR_BRANCH}:${TAG} andrdud/node${CUR_BRANCH}:${TAG}"
                        sh "docker push andrdud/node${CUR_BRANCH}:${TAG}"
                    }
                }
            }
        }
        stage('Deploy') {
            steps {
                script {
                    if (env.CUR_BRANCH == 'main') {
                        DeployToMain([containerName: "node${CUR_BRANCH}", port: "3000", tag: "${TAG}"])
                    } else {
                        DeployToDev([containerName: "node${CUR_BRANCH}", port: "3001", tag: "${TAG}"])
                    }
                }
            }
        }
    }
}
