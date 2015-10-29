# bpms61-ejb-persistence

A simple Web Application deployable in JBoss EAP 6.4.

To run it make sure you create the datasource `java:jboss/datasources/BPMSEjbTestDS` in `standalone.xml`. 

Then you can run `mvn clean package` and deploy the WAR you can find in target directory.

Once you deploy it, a GET to the URL `http://localhost:8080/bpms61-ejb-persistence/rest/process/start` will start a new process instance and you should see `Hello World Process` being printed in the logs.
