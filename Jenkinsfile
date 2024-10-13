pipeline {
    agent any

    environment {
        KIND_BIN = '/var/jenkins_home/kind'
        KUBE_CONTEXT = 'kind-kind-eventmanager' // Cambia aquí para usar el contexto correcto
        KUBECTL_BIN = '/var/jenkins_home/kuber'
    }

    stages {
        stage('Install kubectl') {
            steps {
                script {
                    echo 'Instalando kubectl...'
                    sh '''
                        curl -LO "https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/linux/amd64/kubectl"
                        chmod +x ./kubectl
                        mv ./kubectl ${KUBECTL_BIN}
                        echo "Verificando kubectl..."
                        ${KUBECTL_BIN}/kubectl version --client
                    '''
                }
            }
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
                    // sh 'mvn test' // Descomenta si deseas ejecutar pruebas
                }
            }
        }

        stage('Setup Kind') {
            steps {
                script {
                    echo 'Configurando Kind...'
                    sh '''
                        curl -Lo /tmp/kind https://kind.sigs.k8s.io/dl/v0.20.0/kind-linux-amd64
                        chmod +x /tmp/kind
                        mv /tmp/kind ${KIND_BIN}
                        export PATH=$PATH:${KIND_BIN}
                        kind delete cluster --name kind-kind-eventmanager || echo "No existing cluster to delete"
                        kind create cluster --name kind-kind-eventmanager
                    '''
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    try {
                        echo 'Cargando la imagen en Kind...'
                        sh '''
                            export PATH=$PATH:${KIND_BIN}:${KUBECTL_BIN}
                            kind load docker-image eventmanager:latest --name kind-kind-eventmanager
                        '''
                        
                        echo 'Aplicando despliegues a Kubernetes...'
                        sh '''
                            export PATH=$PATH:${KIND_BIN}:${KUBECTL_BIN}
                            kubectl config use-context ${KUBE_CONTEXT}
                            kubectl apply -f k8s/mysql-deployment.yaml --context ${KUBE_CONTEXT}
                            kubectl apply -f k8s/mysql-service.yaml --context ${KUBE_CONTEXT}
                            kubectl apply -f k8s/eventmanager-deployment.yaml --context ${KUBE_CONTEXT}
                            kubectl apply -f k8s/eventmanager-service.yaml --context ${KUBE_CONTEXT}
                        '''
                    } catch (Exception e) {
                        echo "Falló al cargar la imagen Docker o aplicar los recursos de Kubernetes: ${e.getMessage()}"
                    }
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
