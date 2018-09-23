pipeline {
    agent any
    parameters {
        string(name: 'DevHost', defaultValue: '127.0.0.1', description: 'Host name of DEV server')
        choice(name: 'Orchestrator', choices: ['kubernetes', 'openshift'], description: 'Which orchestrator')
    }
    environment {
        COMMIT_ID = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim();
        ORCHESTRATOR = "${params.Orchestrator}"
        DB_IMAGE = "${params.Orchestrator == 'kubernetes' ? 'postgres:10.4' : 'centos/postgresql-96-centos7'}"
    }
    stages {
        stage('Build') {
            steps {
                sh './mvn_steps.sh build'
            }
        }
        stage('Component tests') {
            steps {
                sh './mvn_steps.sh component_tests'
            }
        }
        stage('Build Images') {
            steps {
                sh "./mvn_steps.sh build_image ${env.COMMIT_ID}"
            }
        }
        stage('Push Images') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub', usernameVariable: 'username', passwordVariable: 'password')]) {
                    sh "./mvn_steps.sh push_image ${env.COMMIT_ID} ${username} ${password}"
                }
            }
        }
        stage("Deploy") {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dev-ssh', usernameVariable: 'username', passwordVariable: 'password')]) {
                    script {
                        remote = [:]
                        remote.name = 'dev'
                        remote.host = "${params.DevHost}"
                        remote.allowAnyHosts = true
                        remote.user = username
                        remote.password = password
                    }

                    sh 'envsubst < templates/deployment.yml.template > templates/deployment.yml'
                    sshPut remote: remote, from: 'templates', into: 'exercises'

                    sshScript remote: remote, script: "deploy/${params.Orchestrator}.sh"
                }
            }
        }
    }
}