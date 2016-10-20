# decision-server-jmsclient-on-brms
--

A simple client for Decision Server (product name for Kie Server) running on the same server where JBoss BRMS and Decision Server are running.

### Building and deploy

Make sure JBoss BRMS on EAP is running locally and using default ports numbers and then run `$ mvn clean package jboss-as:deploy`. 

Wait the deploy to finish and then send HTTP Requests to it. The following curl commands can be used to invoke KieServer using JMS:

~~~
curl -X POST http://localhost:8080/decision-server-jmsclient-on-brms/rest/list-containers-resource/jms
~~~

You can also test the REST client using the follow curl command:

~~~
curl -X POST http://localhost:8080/decision-server-jmsclient-on-brms/rest/list-containers-resource/rest
~~~
