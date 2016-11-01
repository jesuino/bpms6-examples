package org.fxapps.bpms.decisionserver;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.model.KieContainerResource;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;

@Stateless
@Path("list-containers-resource")
public class ListContainersResource {

	private static final String URL = "http://localhost:8080/kie-server/services/rest/server";

	private static final String USER = "kieserver";

	private static final String PSW = "kieserver1!";


	
//	@Resource(mappedName = "java:/JmsXA")
	@Resource(mappedName = "java:/ConnectionFactory")
	private ConnectionFactory connectionFactory;

	@Resource(mappedName = "java:/queue/KIE.SERVER.REQUEST")
	private Queue reqQueue;
	
	@Resource(mappedName = "java:/queue/KIE.SERVER.RESPONSE")
	private Queue respQueue;
	
	
	@POST
	@Path("jms")
	@Produces("application/json")
	public List<KieContainerResource> executeUsingJMS(){
		KieServicesConfiguration conf = KieServicesFactory.newJMSConfiguration(connectionFactory, reqQueue, respQueue, USER, PSW);
		conf.setMarshallingFormat(MarshallingFormat.JSON);
		KieServicesClient client = KieServicesFactory.newKieServicesClient(conf);
		return client.listContainers().getResult().getContainers();
	}
	
	@POST
	@Path("rest")
	@Produces("application/json")
	public List<KieContainerResource> executeUsingREST(){
		KieServicesConfiguration conf = KieServicesFactory.newRestConfiguration(URL, USER, PSW);
		conf.setMarshallingFormat(MarshallingFormat.JSON);
		KieServicesClient client = KieServicesFactory.newKieServicesClient(conf);
		return client.listContainers().getResult().getContainers();
	}

}