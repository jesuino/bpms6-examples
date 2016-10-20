package org.fxapps.bpms.decisionserver;

import java.util.List;

import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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

	private static final String URL = "http://localhost:8180/kie-server/services/rest/server";

	private static final String USER = "kieserver";

	private static final String PSW = "kieserver1!";
	
	@POST
	@Path("jms")
	@Produces("application/json")
	public List<KieContainerResource> executeUsingJMS() throws NamingException{
        // FIRST WAY: Using the initial context
        java.util.Properties env = new java.util.Properties();
        env.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY,
                        "org.jboss.naming.remote.client.InitialContextFactory");
        env.put(javax.naming.Context.PROVIDER_URL, "remote://localhost:4547");
        // the same user used in kie server will be against the remote server -
        // make sure the user has the role "admin" or modify the roles for JMS
        // security in standalone.xml
        env.put(javax.naming.Context.SECURITY_PRINCIPAL, USER);
        env.put(javax.naming.Context.SECURITY_CREDENTIALS, PSW);
        InitialContext ctx = new InitialContext(env);

        // SECOND WAY: Lookup queues to build the configuration
        ConnectionFactory conn = (ConnectionFactory) ctx.lookup("jms/RemoteConnectionFactory");
        Queue reqQueue = (Queue) ctx.lookup("jms/queue/KIE.SERVER.REQUEST");
        Queue respQueue = (Queue) ctx.lookup("jms/queue/KIE.SERVER.RESPONSE");

        // Use the configuration you want
        KieServicesConfiguration conf = KieServicesFactory.newJMSConfiguration(conn, reqQueue, respQueue, USER, PSW);
        //conf = KieServicesFactory.newJMSConfiguration(ctx, USER, PASSWORD);
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