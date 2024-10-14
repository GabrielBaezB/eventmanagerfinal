pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    environment {
        DOCKER_IMAGE_NAME = 'eventmanager:latest'
        SONAR_TOKEN = credentials('sonar-qube-key')
        BUILD_TIMESTAMP = new Date().format("yyyyMMdd-HHmmss")
    }

    stages {
        stage('SCM') {
            steps {
                checkout scm
            }
        }

        stage('Compile') {
            steps {
                script {
                    echo 'Compilando el proyecto con Maven...'
                    sh 'mvn clean package'  // Asegúrate de que el artefacto se construya antes de cualquier análisis o carga
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    def mvn = tool name: 'Maven', type: 'hudson.tasks.Maven$MavenInstallation'
                    withSonarQubeEnv('SonarQube') {
                        sh "${mvn}/bin/mvn sonar:sonar -Dsonar.projectKey=sonar-qube -Dsonar.projectName='con-jenkins' -Dsonar.login=${SONAR_TOKEN}"
                    }
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    echo 'Ejecutando pruebas...'
                    sh 'mvn test'
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

        stage('Upload Artifact') {
            steps {
                script {
                    try {
                        nexusArtifactUploader(
                            nexusVersion: 'nexus3',
                            protocol: 'http',
                            nexusUrl: '192.168.132.189:8081',
                            groupId: 'QA',
                            version: "${env.BUILD_ID}-${env.BUILD_TIMESTAMP}",
                            repository: 'nexus-test',
                            credentialsId: 'NexusLogin',
                            artifacts: [
                                [artifactId: 'eventmanager',
                                classifier: '',
                                file: 'target/eventmanager-0.0.1-SNAPSHOT.jar',
                                type: 'jar']
                            ]
                        )
                    } catch (Exception e) {
                        echo "Error uploading artifact: ${e.message}"
                        throw e
                    }
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
