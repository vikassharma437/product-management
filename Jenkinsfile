def DEV_API_SERVER="https://api.cluster-89e8.89e8.sandbox1804.opentlc.com:6443"
def DEV_NAMESPACE = "dev"
def templatePath = "config/template.json"
def mvnCmd = "mvn -s config/settings.xml"

pipeline{
		agent{
				label 'docker'
		}
    environment {
      	appName = "sample-app"
				appVersion = "0.0.1-SNAPSHOT"
      	buildconf = "false"
    }
		stages {
        stage('Application Code Checkout') {
						steps {
								checkout scm
						}
				}
        stage('Build Common Lib'){
          steps {
            sh "mkdir -p /tmp/common-lib"
            dir("/tmp/common-lib") {
              git branch: 'main',
              credentialsId: 'cicd-github-jenkins',
              url: 'https://github.com/vikassharma437/common-lib.git'
              sh "${mvnCmd} clean deploy -DskipTests=true -Dversion=${env.BUILD_NUMBER}"
            }
          }
        }
				stage('Application Code Compile'){
						steps{
								sh "${mvnCmd} clean compile"
						}
				}
				stage('Execute JUnit Test Cases'){
						steps{
								sh "${mvnCmd} package -Dversion=${env.BUILD_NUMBER}"
								junit "target/surefire-reports/*.xml"
						}
				}
				stage("Upload Artifact to Nexus") {
            steps {
                sh "${mvnCmd} deploy -DskipTests=true -Dversion=${env.BUILD_NUMBER}"
            }
        }
				
        stage('Deploy Template in DEV Namespace') {
  					steps{
  							script {
  									try {
                        openshift.withCluster('devCluster', "${DEV_OCP_PASSWD}") {
                            openshift.withProject("${DEV_NAMESPACE}") {
                              echo "Using project: ${openshift.project()}"
                              echo "Using AppName: ${appName}"
                              sh "oc login -u ${DEV_OCP_USER} -p ${DEV_OCP_PASSWD} ${DEV_API_SERVER} -n ${DEV_NAMESPACE} --insecure-skip-tls-verify=true"
                              buildconf = sh(script: 'oc get bc ${appName} >> /dev/null 2>&1 && echo "true" || echo "false"', returnStdout: true)
                              buildconf = buildconf.trim()
                              echo "BuildConfig status contains: '${buildconf}'"

                              if(buildconf == 'false') {
                                 sh "oc new-app ${templatePath} --as-deployment-config -n ${DEV_NAMESPACE} -p PROJECT=${DEV_NAMESPACE} -p APP_NAME=${appName}"
                              } else {
                                echo "Template is already exist. Hence, skipping this stage."
                              }
                           }
                        }
                     } catch(e) {
                       print e.getMessage()
                       echo "${DEV_NAMESPACE} stage having some issue so this stage can be ignored. Please check logs for more details."
                     }
                }
            }
        }
       
				//Image Deployment
        stage('Deploy Image Build to Dev Namespace') {
			     steps {
				       script {
					          try {
						            timeout(time: 180, unit: 'SECONDS') {
							              openshift.withCluster('devCluster', "${DEV_OCP_PASSWD}") {
								                openshift.withProject(${DEV_NAMESPACE}) {
                                    sh "oc login -u ${DEV_OCP_USER} -p ${DEV_OCP_PASSWD} ${DEV_API_SERVER} -n ${DEV_NAMESPACE} --insecure-skip-tls-verify=true"
                                    sh "oc start-build ${appName} --from-file=target/${appName}-${appVersion}.jar --wait=true"
                                    echo "Build ${appName} deployed successfully in ${DEV_NAMESPACE} namespace"
									              }
							              }
        						    }
        					  } catch(e) {
        						    print e.getMessage()
        						    error "Build not successful"
        					  }
				       }
			    }
		  }
		  stage('Tag Image in Dev Namespace') {
			    steps {
				      script {
                  openshift.withCluster('devCluster', "${DEV_OCP_PASSWD}") {
                      openshift.withProject(${DEV_NAMESPACE}) {
                            sh "oc login -u ${DEV_OCP_USER} -p ${DEV_OCP_PASSWD} ${DEV_API_SERVER} -n ${DEV_NAMESPACE} --insecure-skip-tls-verify=true"
						                sh "oc tag ${DEV_NAMESPACE}/${appName}:latest ${DEV_NAMESPACE}/${appName}:${env.BUILD_NUMBER} -n ${DEV_NAMESPACE}"
					            }
				          }
			        }
          }
		  }
   }
}
