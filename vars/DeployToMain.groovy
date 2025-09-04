def call(Map config = [:]) {
    def containerName = config.containerName ?: "nodemain" 
    def containerStatus = sh(script: "docker ps -a --filter name=^/${containerName}\$ --format '{{.Names}}'", returnStdout: true).trim()
    
    if (containerStatus == containerName) {
        sh "docker container stop ${containerName} || true"
        sh "docker container rm -f ${containerName}"
    }
    withDockerRegistry(credentialsId: 'DOCKER_HUB', url: '') {
        sh "docker run -d --name ${containerName} -p ${config.port}:3000 andrdud/${containerName}:${config.tag}"
    }
}
