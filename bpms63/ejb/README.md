 EJB and jBPM integration example
 --
 This is an example of how to use BPM Suite (jBPM) EJBs WAR.
 
 The projects you will find here are:
 
- `jbpm-ejb-example-kjar`: A kjar with a process used in the application
- `jbpm-ejb-example`: The EJBs application;
- `jbpm-ejb-example-client`: A client application
 
 
## Building the application

I assume you have maven installed and a JBoss EAP 6.4 local installation. So first install the kJAR by running the following command from the project directory: `mvn clean install`.
Then you can go to `jbpm-ejb-example-client` and `jbpm-ejb-example` and do the same thing.

## Running

Start JBoss EAP in domain mode and then run the following on `jbpm-ejb-example`: 

`mvn clean install jboss-as:deploy -Dtarget.server.group=main-server-group`

Do the same for `jbpm-ejb-example-client`.

Observe the logs. Once the client is deployed, it will deploy the kjar we build earlier. Now we can start the process and handle its tasks.

Assuming you are using JBoss EAP 6.4 out of box configuration, you should be able to send a request to start the process on the first server and complete the tasks on the second server. So when you call this you start a new process:

~~~
curl -X POST http://localhost:8080/jbpm-ejb-example-client/process
~~~

It returns the piid that you can use on a second call, let's say on the second server for the main-server-group:

~~~
curl -X POST 'http://localhost:8230/jbpm-ejb-example-client/task?piid=3'
~~~

To turn on clustering, rename the file `jboss-ejb3.xml.bkp` on `jbpm-ejb-example`  to `jboss-ejb3.xml`.
