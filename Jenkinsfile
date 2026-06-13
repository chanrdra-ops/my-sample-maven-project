pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    // Triggers the execution whenever GitHub fires a webhook event
    triggers {
        githubPush()
    }

    parameters {
        choice(name: 'ENVIRONMENT', choices: ['qa', 'dev', 'stage'], description: 'Test environment')
        choice(name: 'BROWSER', choices: ['chrome', 'firefox', 'edge'], description: 'Browser')
        booleanParam(name: 'HEADLESS', defaultValue: true, description: 'Run browser in headless mode')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout([$class: 'GitSCM',
                    branches: [[name: '*/main']],
                    userRemoteConfigs: [[
                        url: 'https://github.com',
                        credentialsId: 'github-pat-token'
                    ]]
                ])
            }
        }

        stage('Test') {
            steps {
                script {
                    def command = "mvn -B clean test -Denv=${params.ENVIRONMENT} -Dbrowser=${params.BROWSER} -Dheadless=${params.HEADLESS}"
                    
                    // Dynamically bind the correct Maven home folder based on the OS
                    if (isUnix() && sh(script: "uname", returnStdout: true).contains("Darwin")) {
                        withMaven(maven: 'Maven3_Mac') {
                            sh command
                        }
                    } else if (isUnix()) {
                        withMaven(maven: 'Maven3_Linux') {
                            sh command
                        }
                    } else {
                        // For Windows fallback environments
                        withMaven(maven: 'Maven3_Mac') {
                            bat command
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
            archiveArtifacts allowEmptyArchive: true, artifacts: 'target/reports/**/*, target/screenshots/**/*, target/surefire-reports/**/*'
        }
        success {
            script {
                try {
                    def jiraIssues = jiraGetIssuesFromScm()

                    if (jiraIssues != null && !jiraIssues.isEmpty()) {
                        for (issue in jiraIssues) {
                            echo "Found Jira ticket in commit log: ${issue}"
                            jiraTransitionIssue idOrKey: issue, transitionName: 'Done'
                            jiraAddComment idOrKey: issue, comment: "Jenkins Automation Build #${env.BUILD_NUMBER} passed successfully! Moving task to Done."
                        }
                    } else {
                        echo "No valid Jira issue keys detected inside the Git commit message logs."
                    }
                } catch (Exception e) {
                    echo "Jira status transition skipped: Ensure the Jira Steps plugin is configured. Error: ${e.getMessage()}"
                }
            }
        }
    }
}
