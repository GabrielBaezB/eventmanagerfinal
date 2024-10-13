pipeline {
    agent any

    environment {

    }

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
                    sh 'docker build -t eventmanager:latest .'
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    echo 'Ejecutando pruebas...'
                }
            }
        }

    post {
        success {
            echo 'Pipeline completado con éxito.'
        }
        failure {
            echo 'El pipeline falló.'
        }
    }
}
