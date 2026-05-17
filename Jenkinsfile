pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    parameters {
        choice(name: 'ENVIRONMENT', choices: ['qa', 'dev', 'stage'], description: 'Test environment')
        choice(name: 'BROWSER', choices: ['chrome', 'firefox', 'edge'], description: 'Browser')
        booleanParam(name: 'HEADLESS', defaultValue: true, description: 'Run browser in headless mode')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Test') {
            steps {
                script {
                    def command = "mvn -B clean test -Denv=${params.ENVIRONMENT} -Dbrowser=${params.BROWSER} -Dheadless=${params.HEADLESS}"
                    if (isUnix()) {
                        sh command
                    } else {
                        bat command
                    }
                }
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
            archiveArtifacts allowEmptyArchive: true, artifacts: 'target/reports/**, target/screenshots/**, target/surefire-reports/**'
        }
    }
}
