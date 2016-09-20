package org.jbpm.ejb.client.example;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.jbpm.services.ejb.api.DeploymentServiceEJBRemote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Startup
public class StartupBean {
	
	static final String DEPLOYMENT_ID = "example:jbpm-ejb-example-kjar:1.0";
	
	@EJB(lookup="java:global/jbpm-ejb-example/DeploymentServiceEJBImpl!org.jbpm.services.ejb.api.DeploymentServiceEJBRemote")
	DeploymentServiceEJBRemote deploymentService;
	
	Logger log = LoggerFactory.getLogger(StartupBean.class);
	

	@PostConstruct
	public void init() {
		log.info("DeploymentService: " + deploymentService);
		boolean deployed = deploymentService.isDeployed(DEPLOYMENT_ID);
		if(!deployed) {			
			log.info("Deploying " + DEPLOYMENT_ID);
			String deploymentParts[] = DEPLOYMENT_ID.split(":");
			String groupId = deploymentParts[0];
			String artifactId = deploymentParts[1];
			String version = deploymentParts[2];
			deploymentService.deploy(groupId, artifactId, version);
		} else {
			log.info(DEPLOYMENT_ID + " already deployed."); 
		}
	}
}