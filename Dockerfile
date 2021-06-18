FROM registry.redhat.io/openshift4/ose-jenkins-agent-maven
COPY --chown=1000720000:1000720000 config.json /home/jenkins/.docker/config.json
USER root RUN chmod 777 /home/jenkins/.docker/config.json
USER 1001
