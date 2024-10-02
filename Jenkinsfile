pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
        stage('Docker Build') {
            steps {
                sh 'docker build -t eventmanager .'
            }
        }
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar -Dsonar.projectKey=eventmanager'
                }
            }
        }
        stage('Kubernetes Deploy') {
            steps {
                sh 'kind load docker-image eventmanager --name eventmanager'
                sh 'kubectl apply -f k8s/deployment.yml'
                sh 'kubectl apply -f k8s/service.yml'
            }
        }
    }
}
