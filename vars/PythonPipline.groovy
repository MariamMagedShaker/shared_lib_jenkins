def call() {
    pipeline {
        agent any

        environment {
            DOCKER_USER = credentials('docker_user')
            DOCKER_PASS = credentials('docker_pass')

            def XYZ='Hello Mariam'
        }

        stages{
        
            stage("build Docker image"){
                steps{
                    
 
                    script{

                        def dockerx = new org.ITI.docker()
                        dockerx.build("mariam191/python_app", "${BUILD_NUMBER}")
                    }
                    
                }
            }

            stage("push Python app image"){
                steps{
                    script{

                        def dockerx = new org.ITI.docker()
                        dockerx.login("${DOCKER_USER}", "${DOCKER_PASS}")
                        dockerx.push("mariam191/python_app", "${BUILD_NUMBER}")
                    }
                    
                    
                }
            }

        }
    }
}
    
 