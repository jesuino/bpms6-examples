package org.jbpm.ejb.client.example;

import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.jbpm.services.ejb.api.ProcessServiceEJBRemote;
import org.jbpm.services.ejb.api.RuntimeDataServiceEJBRemote;
import org.jbpm.services.ejb.api.UserTaskServiceEJBRemote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/")
@Stateless
public class ProcessResource {
	
	
	private static final String USER_ID = "bpmsAdmin";

	private static final String PROCESS_DEF = "project1.ht";

	@EJB(lookup = "java:global/jbpm-ejb-example/ProcessServiceEJBImpl!org.jbpm.services.ejb.api.ProcessServiceEJBRemote")
    private ProcessServiceEJBRemote processService;
	
    @EJB(lookup = "java:global/jbpm-ejb-example/UserTaskServiceEJBImpl!org.jbpm.services.ejb.api.UserTaskServiceEJBRemote")
    private UserTaskServiceEJBRemote userTaskService;
    
    @EJB(lookup = "java:global/jbpm-ejb-example/RuntimeDataServiceEJBImpl!org.jbpm.services.ejb.api.RuntimeDataServiceEJBRemote")
    private RuntimeDataServiceEJBRemote runtimeDataService;
    
	Logger log = LoggerFactory.getLogger(ProcessResource.class);


	@POST
	public void startProcessAndCompleteTask () {
		log.info("Starting process " + PROCESS_DEF);
		Long piid = processService.startProcess(StartupBean.DEPLOYMENT_ID, PROCESS_DEF);
		log.info("Process started with ID " + piid);
		List<Long> tasks = runtimeDataService.getTasksByProcessInstanceId(piid);
		for (Long taskId : tasks) {
			log.info("Starting and completing taskId " + taskId);
			userTaskService.start(taskId, USER_ID);
			userTaskService.complete(taskId, USER_ID, null);
		}
		log.info("Process instance " + piid + " finished.");
	}
}
