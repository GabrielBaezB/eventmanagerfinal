pipeline {
    agent any

    tools {
        maven 'Maven' // Asegúrate de que este nombre coincida con la configuración en Jenkins
    }

    environment {
        // Define any necessary environment variables here
        DOCKER_IMAGE_NAME = 'eventmanager:latest'
        SONAR_TOKEN = credentials('sonar-qube-key') // Reemplaza 'sonar-qube-key' con el ID de tus credenciales
        BUILD_TIMESTAMP = new Date().format("yyyyMMdd-HHmmss")
    }

    stages {
        stage('SCM') {
            steps {
                checkout scm
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    def mvn = tool name: 'Maven', type: 'hudson.tasks.Maven$MavenInstallation'
                    withSonarQubeEnv('SonarQube') {
                        sh "${mvn}/bin/mvn clean verify sonar:sonar -Dsonar.projectKey=sonar-qube -Dsonar.projectName='con-jenkins' -Dsonar.login=${SONAR_TOKEN}"
                    }
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
                            nexusUrl: '192.168.132.189:8081', // Asegúrate de que esta URL sea correcta
                            groupId: 'QA',
                            version: "${env.BUILD_ID}-${env.BUILD_TIMESTAMP}",
                            repository: 'nexus-test',
                            credentialsId: 'NexusLogin',
                            artifacts: [
                                [artifactId: 'eventmanager',
                                classifier: '',
                                file: 'target/eventmanager.jar',
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
