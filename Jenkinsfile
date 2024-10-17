pipeline {
   agent any 
    environment {
        DOCKER_IMAGE = 'ingrid911/producto:tag' // Cambia esto por el nombre de tu imagen
    }
    stages {
        stage('Login into Docker Hub') {
            steps {
                script {
                    // Aquí defines tus credenciales directamente
                    def dockerUsername = 'ingrid911' // Reemplaza con tu nombre de usuario de Docker Hub
                    def dockerPassword = 'ingrid911' // Reemplaza con tu contraseña de Docker Hub
                    
                    // Inicia sesión en Docker Hub utilizando las credenciales
                    sh "echo ${dockerPassword} | docker login -u ${dockerUsername} --password-stdin"
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    // Construir la imagen Docker después de iniciar sesión
                    echo 'Building Docker image...'
                    docker.build(DOCKER_IMAGE)
                }
            }
        }
        stage('Push to Docker Hub') {
            steps {
                script {
                    // Empujar la imagen Docker a Docker Hub
                    echo 'Pushing Docker image to Docker Hub...'
                    docker.image(DOCKER_IMAGE).push()
                }
            }
        }
        stage('Deploy to Kubernetes') {
            steps {
                script {
                    echo 'Deploying to Kubernetes...'
                    // Usar kubectl para desplegar la imagen en Kubernetes
                    sh "kubectl apply -f ../deployment.yaml" // Cambia esta ruta al lugar donde se encuentra tu archivo YAML
                    }
                }
            }
        }
    }
    post {
        success {
            echo 'Build and deployment was successful!'
            // Aquí puedes agregar notificaciones adicionales si es necesario
        }
        failure {
            echo 'Build or deployment failed!'
            // Notificar sobre el error
        }
    }
}
