pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    // Triggers the execution Cehckong data whenever GitHub fires a webhook event
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
                        url: 'https://github.com/chanrdra-ops/my-sample-maven-project.git',
                        credentialsId: 'github-pat-token'
                    ]]
                ])
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
            archiveArtifacts allowEmptyArchive: true, artifacts: 'target/reports/**/*, target/screenshots/**/*, target/surefire-reports/**/*'
        }
        success {
            script {
                try {
                    // Extract Jira Issue Keys (e.g., QA-123) from the triggering Git commit message
                    def jiraIssues = jiraGetIssuesFromScm()

                    if (jiraIssues != null && !jiraIssues.isEmpty()) {
                        for (issue in jiraIssues) {
                            echo "Found Jira ticket in commit log: ${issue}"

                            // Transitions the identified Jira story straight to 'Done'
                            jiraTransitionIssue idOrKey: issue, transitionName: 'Done'

                            // Leaves an audit comment trace detailing the test results inside the issue
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
