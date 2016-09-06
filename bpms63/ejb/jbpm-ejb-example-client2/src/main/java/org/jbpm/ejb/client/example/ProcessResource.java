package org.jbpm.ejb.client.example;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.jbpm.services.ejb.api.ProcessServiceEJBRemote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/")
@Stateless
public class ProcessResource {
	
	private static final String PROCESS_DEF = "project1.ht";

	@EJB(lookup = "java:global/jbpm-ejb-example/ProcessServiceEJBImpl!org.jbpm.services.ejb.api.ProcessServiceEJBRemote")
    private ProcessServiceEJBRemote processService;
    
	Logger log = LoggerFactory.getLogger(ProcessResource.class);

	@POST
	public long startProcess () {
		log.info("Starting process " + PROCESS_DEF);
		Long piid = processService.startProcess(StartupBean.DEPLOYMENT_ID, PROCESS_DEF);
		log.info("Process started with ID " + piid);
		return piid;
	}
}
