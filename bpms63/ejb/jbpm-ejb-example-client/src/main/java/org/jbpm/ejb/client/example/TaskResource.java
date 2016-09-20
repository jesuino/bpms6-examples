package org.jbpm.ejb.client.example;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.jbpm.services.ejb.api.RuntimeDataServiceEJBRemote;
import org.jbpm.services.ejb.api.UserTaskServiceEJBRemote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/task")
@Stateless
public class TaskResource {
	
	
	private static final String USER_ID = "bpmsAdmin";

	
    @EJB(lookup = "java:global/jbpm-ejb-example/UserTaskServiceEJBImpl!org.jbpm.services.ejb.api.UserTaskServiceEJBRemote")
    private UserTaskServiceEJBRemote userTaskService;
    
    @EJB(lookup = "java:global/jbpm-ejb-example/RuntimeDataServiceEJBImpl!org.jbpm.services.ejb.api.RuntimeDataServiceEJBRemote")
    private RuntimeDataServiceEJBRemote runtimeDataService;
    
	Logger log = LoggerFactory.getLogger(TaskResource.class);


	@POST
	public void startProcessAndCompleteTask (@QueryParam("piid") long piid) {
		log.info("Getting tasks for piid " + piid);
		List<Long> tasks = runtimeDataService.getTasksByProcessInstanceId(piid);
		for (Long taskId : tasks) {
			log.info("Claiming, starting and completing taskId " + taskId);
			userTaskService.claim(taskId, USER_ID);
			userTaskService.start(taskId, USER_ID);
			userTaskService.complete(taskId, USER_ID, null);
		}
		log.info("Tasks for process instance " + piid + " finished.");
	}
}
