pipeline {
    agent any

    stages {
        stage('Compile') {
            steps {
                script {
                    // Compilar el proyecto y empaquetarlo en un JAR
                    sh 'mvn clean package'
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    // Construir la imagen Docker
                    sh 'docker build -t eventmanager:latest .'
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    // Aquí puedes agregar tus comandos para ejecutar pruebas, si es necesario
                    echo 'Ejecutando pruebas...'
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    // Cargar la imagen a Kind
                    sh 'kind load docker-image eventmanager:latest --name eventmanager'
                    // Aplicar despliegue a Kubernetes
                    sh 'kubectl apply -f k8s/mysql-deployment.yaml'
                    sh 'kubectl apply -f k8s/mysql-service.yaml'
                    sh 'kubectl apply -f k8s/eventmanager-deployment.yaml'
                    sh 'kubectl apply -f k8s/eventmanager-service.yaml'
                }
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
