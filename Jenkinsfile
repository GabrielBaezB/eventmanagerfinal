pipeline {
    agent any

    environment {
        // Define any necessary environment variables here
        DOCKER_IMAGE_NAME = 'eventmanager:latest'
    }

    stages {
        stage('Compile') {
            steps {
                script {
                    echo 'Compilando el proyecto con Maven...'
                    sh 'mvn clean package'
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    echo 'Construyendo la imagen Docker...'
                    sh "docker build -t ${DOCKER_IMAGE_NAME} ."
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    echo 'Ejecutando pruebas...'
                    sh 'mvn test'  // Run tests using Maven
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline completado con éxito.'
            // Add any notifications here
        }
        failure {
            echo 'El pipeline falló.'
            // Add any notifications here
        }
        cleanup {
            echo 'Limpiando recursos...'
            // Include cleanup steps if needed
        }
    }
}
