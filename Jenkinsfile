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
                    // Set the PATH for the current shell
                    sh '''
                        export PATH=$PATH:${KIND_BIN}
                        if kind get clusters | grep -q "${KUBE_CONTEXT}"; then
                            echo "The cluster already exists."
                        else
                            kind create cluster --name ${KUBE_CONTEXT}
                        fi
                    '''
                }
            }
        }
        stage('Deploy') {
            steps {
                script {
                    // Load the Docker image into Kind and apply configurations
                    sh '''
                        export PATH=$PATH:${KIND_BIN}
                        kind load docker-image eventmanager:latest --name ${KUBE_CONTEXT}

                        kubectl apply -f k8s/mysql-deployment.yaml --context ${KUBE_CONTEXT}
                        kubectl apply -f k8s/mysql-service.yaml --context ${KUBE_CONTEXT}
                        kubectl apply -f k8s/eventmanager-deployment.yaml --context ${KUBE_CONTEXT}
                        kubectl apply -f k8s/eventmanager-service.yaml --context ${KUBE_CONTEXT}
                    '''
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
