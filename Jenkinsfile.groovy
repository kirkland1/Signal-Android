pipeline {
    options {
        disableConcurrentBuilds()
    }
    agent none
    environment {
        ANDROID_SDK_ROOT='/home/ubuntu/sdk'
    }
    stages {

        stage('Build') {
            agent { label 'master'}
            steps {
                script {
                    deleteDir()
                    git 'git@github.com:teej2542/Signal-Android-test.git'

                    try {
                        sh "./gradlew test collectResults -x lintchecks:test --continue"
                    } catch (ignored) {
                        sh "mv unit-tests.csv unit-tests-${env.BUILD_NUMBER}.csv"
                        archiveArtifacts artifacts: "unit-tests-${env.BUILD_NUMBER}.csv", followSymlinks: false
                        junit '**/build/test-results/**/*.xml'
                    } finally {
                        echo "done!"
//                         sh "mv unit-tests.csv unit-tests-${env.BUILD_NUMBER}.csv"
//                         archiveArtifacts artifacts: "unit-tests-${env.BUILD_NUMBER}.csv", followSymlinks: false
//                         junit '**/build/test-results/**/*.xml'
                       
                    }
                }
            }
        }
    }
}

