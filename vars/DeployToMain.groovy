def call(Map config = [:]){

pipeline {
    agent any
    
    stages {
        stage('deploy') {
            steps {
                script {
                        def containerStatus = sh(script: "docker ps -a --filter name=^/${config.conteinerName}\$ --format '{{.Names}}'", returnStdout: true).trim()
                        
                        if (containerStatus == containerName) { it."
                            sh "docker container stop ${config.conteinerName} || true"
                            sh "docker container rm -f ${config.conteinerName}"
                        }
                            withDockerRegistry(credentialsId: 'DOCKER_HUB', url: '') {
			sh "docker run -d --name ${config.conteinerName} -p ${config.port}:3000 andrdud/${config.conteinerName}:${config.tag}"}
                            
                }
            }
        }
    }
}


}
