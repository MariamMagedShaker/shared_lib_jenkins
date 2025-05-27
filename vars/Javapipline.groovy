def call(){
    pipeline{

    agent any

    environment{
        DOCKER_USER = credentials('docker_user')
        DOCKER_PASS = credentials('docker_pass')
    }


    parameters {
        string defaultValue: '${BUILD_NUMBER}', description: 'Enter the version of the docker image', name: 'VERSION'
        choice choices: ['true', 'false'], description: 'Skip test', name: 'TEST'
    }

    stages{

        stage("Build java app"){
            steps{
                sh "mvn clean package install"
                
            }
        }
        stage("Build AlL"){
            parallel{
                stage("Test java app"){

                    steps{
                        sh "mvn test -Dmaven.test.skip=${TEST}"    
                    }
                }
            
                stage("build Docker image"){
                    steps{
                        script{
                            sayHello "Mero!!!!!!!"

                            def dockerx = new org.ITI.docker()
                            dockerx.build("mariam191/java_app", "${VERSION}")
                        }
                        
                    }
                }
            }
        }
        

        stage("push java app image"){
            steps{
                 script{
                    def VMIP = vmip()
                    sh "echo ${VMIP}"

                    def dockerx = new org.ITI.docker()
                    dockerx.login("${DOCKER_USER}", "${DOCKER_PASS}")
                    dockerx.push("mariam191/java_app", "${VERSION}")
                }
                
                
            }
        }

    }
    
    post{
        always{
            sh "echo 'Clean the Workspace'"
            cleanWs()
        }
        failure {
            sh "echo 'failed'"
        }
    }
}

}