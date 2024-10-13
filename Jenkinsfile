pipeline {
    agent any

    environment {
        KIND_BIN = '/var/jenkins_home' // Asegúrate de que este directorio tenga permisos
        KUBE_CONTEXT = 'kind-eventmanager' // Cambia este nombre si usas un nombre diferente
    }

    stages {
        stage('Compile') {
            steps {
                script {
                    // Ejecutar la compilación usando Maven
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
        stage('Setup Kind') {
            steps {
                script {
                    // Instalar Kind
                    sh '''
                        curl -Lo /tmp/kind https://kind.sigs.k8s.io/dl/v0.20.0/kind-linux-amd64
                        chmod +x /tmp/kind
                        mv /tmp/kind ${KIND_BIN}
                        export PATH=$PATH:${KIND_BIN}
                        kind create cluster --name ${KUBE_CONTEXT} || echo "El clúster ya existe"
                    '''
                }
            }
        }
        stage('Deploy') {
            steps {
                script {
                    // Cargar la imagen en Kind
                    sh "${KIND_BIN} load docker-image eventmanager:latest --name ${KUBE_CONTEXT}"
                    
                    // Aplicar despliegue a Kubernetes
                    sh 'kubectl apply -f k8s/mysql-deployment.yaml --context ${KUBE_CONTEXT}'
                    sh 'kubectl apply -f k8s/mysql-service.yaml --context ${KUBE_CONTEXT}'
                    sh 'kubectl apply -f k8s/eventmanager-deployment.yaml --context ${KUBE_CONTEXT}'
                    sh 'kubectl apply -f k8s/eventmanager-service.yaml --context ${KUBE_CONTEXT}'
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
