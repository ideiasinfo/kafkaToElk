podTemplate(label: 'java11pod', containers: [
    containerTemplate(name: 'git', image: 'alpine/git', ttyEnabled: true, command: 'cat'),
    containerTemplate(name: 'maven', image: 'maven:3.6.3-jdk-11', command: 'cat', ttyEnabled: true),
    containerTemplate(name: 'docker', image: 'docker', command: 'cat', ttyEnabled: true),
    containerTemplate(name: 'kubectl', image: 'ideiasinfo/jnlp-kubectl:1.0.0', command: 'cat', ttyEnabled: true)
  ],
  volumes: [
    hostPathVolume(mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock'),
    hostPathVolume(mountPath: '/root/.ssh', hostPath: '/var/git/.ssh'),
    hostPathVolume(mountPath: '/root/.m2', hostPath: '/var/maven/.m2'),
    hostPathVolume(mountPath: '/root/.docker', hostPath: '/var/docker/.docker')
  ]
  ) {
    node('java11pod') {
        stage('Check running containers') {
            container('docker') {
                sh 'hostname'
                sh 'hostname -i'
                sh 'docker ps'
            }
        }

        stage('Clone repository') {
            container('git') {
                sh 'hostname -i'
                sh 'git clone -b main git@github.com:ideiasinfo/kafkaToElk.git'
            }
        }

        stage('Maven Build') {
            container('maven') {
                dir('kafkaToElk/') {
                    sh 'hostname'
                    sh 'hostname -i'
                    sh "mvn clean install"
                    script {
                        pom = readMavenPom(file: 'pom.xml')
                        projectVersion = pom.getVersion()
                    }
                }
            }
        }

        stage('Docker Build') {
            container('maven') {
                dir('kafkaToElk/') {
                    sh 'hostname'
                    sh 'hostname -i'
                    sh 'mvn docker:build'
                }
            }
        }

        stage('Docker Login') {
            container('docker') {
                sh 'hostname'
                sh 'hostname -i'
                sh 'docker login'
            }
        }

        stage('Docker Push') {
            container('docker') {
                sh 'hostname'
                sh 'hostname -i'
                sh 'docker images'
                sh 'docker push ideiasinfo/replicator-server:'+projectVersion
                sh 'docker rmi -f ideiasinfo/replicator-server:'+projectVersion
            }
        }

        stage('Deploy') {
            container('kubectl') {
                dir('kafkaToElk/') {
                    sh 'hostname'
                    sh 'hostname -i'
                    sh 'ls'
                    sh 'kubectl apply -f app.yml --namespace default'
                }
            }
        }

    }
}