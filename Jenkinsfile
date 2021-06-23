#!bash
def templatePath = "/tmp/workspace/cicd-sample-app-dev/config/template.json"
def mvnCmd = "mvn -s /tmp/workspace/cicd-sample-app-dev/config/settings.xml"

pipeline{
    agent{
	label 'docker'
    }
    environment {
      	appName = "sample-app"
	appVersion = "0.0.1-SNAPSHOT"
      	buildconf = "false"
	DEV_API_SERVER = "https://api.cluster-89e8.89e8.sandbox1804.opentlc.com:6443"
	DEV_NAMESPACE = "dev"
    }
    stages {
        stage('Parent Build'){
          steps {
            sh "mkdir -p /tmp/common-lib"
            dir("/tmp/common-lib") {
              git branch: 'main',
              credentialsId: 'git-credential',
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
	      	  echo "Using project: ${openshift.project()}"
	       	  echo "Using AppName: ${appName}"
	          sh('oc login -u $DEV_OCP_USER -p $DEV_OCP_PASSWD ${DEV_API_SERVER} -n ${DEV_NAMESPACE} --insecure-skip-tls-verify=true')
	          buildconf = sh(script: 'oc get bc ${appName} >> /dev/null 2>&1 && echo "true" || echo "false"', returnStdout: true)
	          buildconf = buildconf.trim()
	          echo "BuildConfig status contains: '${buildconf}'"

	          if(buildconf == 'false') {
		    sh "oc new-app ${templatePath} -n ${DEV_NAMESPACE} -p PROJECT=${DEV_NAMESPACE} -p APP_NAME=${appName}"
	          } else {
		    echo "Template is already exist. Hence, skipping this stage."
	          }
               } catch(e) {
                 print e.getMessage()
                 error "${DEV_NAMESPACE} stage having some issue so this stage can be ignored. Please check logs for more details."
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
                  sh('oc login -u $DEV_OCP_USER -p $DEV_OCP_PASSWD ${DEV_API_SERVER} -n ${DEV_NAMESPACE} --insecure-skip-tls-verify=true')
                  sh "oc start-build ${appName} --from-file=target/${appName}-${appVersion}.jar --wait=true"
                  echo "Build ${appName} deployed successfully in ${DEV_NAMESPACE} namespace"
		}
              } catch(e) {
                print e.getMessage()
        	error "Build not successful"
              }
	    }
	  }
        }
        stage('Tag Image in Development Project') {
	  steps {
	    script {
              sh('oc login -u $DEV_OCP_USER -p $DEV_OCP_PASSWD ${DEV_API_SERVER} -n ${DEV_NAMESPACE} --insecure-skip-tls-verify=true')
	      sh "oc tag ${DEV_NAMESPACE}/${appName}:latest ${DEV_NAMESPACE}/${appName}:${env.BUILD_NUMBER} -n ${DEV_NAMESPACE}"
	    }
	 }
       }
    }
}
