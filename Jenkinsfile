pipeline {
    environment {
        DOCKERHUB_CREDENTIALS = credentials('docker-token')
    }
    agent any
    triggers {
        pollSCM('* * * * *')
    }
    stages {
        stage("Compile") {
            steps {
                dir('springboot'){
                   sh "./gradlew compileJava -x test"
                }
            }
        }
        stage("Build") {
            steps {
                dir('springboot'){
                   sh "./gradlew build -x test" 
                }
                sh """
                     cp ./springboot/build/libs/MiniBoard-0.0.1-SNAPSHOT.jar ./docker/miniboard/
                     ls -lah ./docker/miniboard/
                   """
            }
        }
        stage("Docker Login") {
            steps {
                sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
            }
        }
        stage("Docker Image Build") {
            steps {
                sh "docker build -t redleon1/apache2_miniboard:${BUILD_NUMBER} ./docker/apache2/"
                sh "docker build -t redleon1/miniboard_miniboard:${BUILD_NUMBER} ./docker/miniboard/"
                sh "docker build -t redleon1/mariadb_miniboard:${BUILD_NUMBER} ./docker/mariadb/"
            }
        }
        stage("Docker Image Push") {
            steps {
                sh "docker push redleon1/apache2_miniboard:${BUILD_NUMBER}"
                sh "docker push redleon1/miniboard_miniboard:${BUILD_NUMBER}"
                sh "docker push redleon1/mariadb_miniboard:${BUILD_NUMBER}"
            }
        }
        stage("Docker Image Clean up") {
            steps {
                sh "docker rmi redleon1/apache2_miniboard:${BUILD_NUMBER}"
                sh "docker rmi redleon1/miniboard_miniboard:${BUILD_NUMBER}"
                sh "docker rmi redleon1/mariadb_miniboard:${BUILD_NUMBER}"
            }
        }
        stage("Deploy") {
            steps {
                sh "sed -i 's/{{VERSION}}/${BUILD_NUMBER}/g' ./kubernetes/apache2.yml"
                sh "sed -i 's/{{VERSION}}/${BUILD_NUMBER}/g' ./kubernetes/miniboard.yml"
                sh "sed -i 's/{{VERSION}}/${BUILD_NUMBER}/g' ./kubernetes/mariadb.yml"
                //sh "kubectl delete --ignore-not-found=true -A ValidatingWebhookConfiguration ingress-nginx-admission"
                sh "kubectl apply -f ./kubernetes/mariadb.yml"
                sh "kubectl apply -f ./kubernetes/miniboard.yml"
                sh "kubectl apply -f ./kubernetes/apache2.yml"
                sh "kubectl apply -f ./kubernetes/ingress.yml"
                // 배포 완료 후 쿠버네티스에서 Ingress IP를 가져와 젠킨스 변수에 할당 (.trim()으로 공백 제거)
                script {
                    env.INGRESS_IP = sh(
                        script: "kubectl get svc ingress-nginx-controller -n ingress-nginx -o jsonpath='{.status.loadBalancer.ingress[0].ip}'",
                        returnStdout: true
                    ).trim()

                    // 젠킨스 빌드 콘솔 로그에 IP 출력
                    echo "========================================="
                    echo "배포 완료! Ingress IP: ${env.INGRESS_IP}"
                    echo "========================================="
                }

            }
            post {
                success {
                    slackSend(channel: "#it교육", color: "#2C953C", message: "miniboard 배포가 성공하였습니다.")
                }
                failure {
                    slackSend(channel: "#it교육", color: "#FF3232", message: "miniboard 배포가 실패하였습니다.")
                }
            }
        }
    }
}
