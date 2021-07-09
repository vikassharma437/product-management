# CICD Demo with Jenkins on OpenShift
This repo is used for CICD demo using Jenkins for continous delivery on Openshift which build and deploy the sample spring boot product management application.

This demo creates:

1. DEV, SIT, UAT, PERF and TRAINING Namespaces in two different clusters.
2. Jenkins pipeline for building the sample-app image on every git commit using Github webhook.
3. Sonatype Nexus OSS with username: admin and password: admin@123.
4. Promote the build image to Nexus (Nexus Integration with Jenkins).

# Prerequisite for CICD
1. Two Openshift clusters.
2. Install Jenkins with Persistent Storage on one cluster.
3. Install Nexus Sonatype OSS on another cluster.
4. Create Dev, SIT and training namespace on cluster where Nexus is installed.
5. Create UAT and PERF namespace on cluster where Jenkins is Installed.
