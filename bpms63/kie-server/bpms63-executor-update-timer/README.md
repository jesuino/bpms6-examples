Update Process Timer on Kie Server
--

Sample commands showing how to skip timer on process instances running on Kie Server.

This project uses Java 8, so kie server server must run with java 8.

### Building and deploying the project

- Build the project with `mvn clean package`- Copy the jar from target dir to `kie-server.war/WEB-INF/lib`- Start the server and make a request to kie server to start a job that will run the custom command. Provide the processInstanceId and container id(the container where the process instance is running), for example:POST on `http://localhost:8080/kie-server/services/rest/server/jobs` with payload: 

~~~
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<job-request-instance>
    <job-command>example.executor_update_timer.commands.SetTimerAsTriggeredCommand</job-command>
    <scheduled-date>2016-12-19T00:00:00-02:00</scheduled-date>
    <data>
        <entry>
            <key>identifier</key>
            <value xsi:type="xs:string" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">timer</value>
        </entry>
        <entry>
            <key>processInstanceId</key>
            <value xsi:type="xs:string" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">5</value>
        </entry>
    </data>
</job-request-instance>
~~~
- You should see the command being executed and the process instance continue its execution after it

