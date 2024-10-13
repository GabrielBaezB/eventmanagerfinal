pipeline {
    agent any

    environment {
        KIND_BIN = '/var/jenkins_home/kind' // Path to the kind binary
        KUBE_CONTEXT = 'kind-eventmanager' // Change this name if you are using a different name
    }

    stages {
        stage('Compile') {
            steps {
                script {
                    // Execute the build using Maven
                    sh 'mvn clean package'
                }
            }
        }
        stage('Build') {
            steps {
                script {
                    // Build the Docker image
                    sh 'docker build -t eventmanager:latest .'
                }
            }
        }
        stage('Test') {
            steps {
                script {
                    // Add commands to run tests if necessary
                    echo 'Running tests...'
                }
            }
        }
        stage('Setup Kind') {
            steps {
                script {
                    // Install Kind
                    sh '''
                        curl -Lo /tmp/kind https://kind.sigs.k8s.io/dl/v0.20.0/kind-linux-amd64
                        chmod +x /tmp/kind
                        mv /tmp/kind ${KIND_BIN}
                    '''
                }
            }
        }
        stage('Create Kind Cluster') {
            steps {
                script {
                    // Ensure KIND_BIN is in the PATH for subsequent steps
                    sh 'export PATH=$PATH:${KIND_BIN} && kind create cluster --name ${KUBE_CONTEXT} || echo "The cluster already exists"'
                }
            }
        }
        stage('Deploy') {
            steps {
                script {
                    // Load the Docker image into Kind
                    sh "kind load docker-image eventmanager:latest --name ${KUBE_CONTEXT}"
                    
                    // Apply deployment to Kubernetes
                    sh "kubectl apply -f k8s/mysql-deployment.yaml --context ${KUBE_CONTEXT}"
                    sh "kubectl apply -f k8s/mysql-service.yaml --context ${KUBE_CONTEXT}"
                    sh "kubectl apply -f k8s/eventmanager-deployment.yaml --context ${KUBE_CONTEXT}"
                    sh "kubectl apply -f k8s/eventmanager-service.yaml --context ${KUBE_CONTEXT}"
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully.'
        }
        failure {
            echo 'The pipeline failed.'
        }
    }
}
