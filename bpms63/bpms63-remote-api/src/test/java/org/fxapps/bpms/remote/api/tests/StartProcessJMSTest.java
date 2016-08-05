package org.fxapps.bpms.remote.api.tests;

import java.util.Properties;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.remote.client.api.RemoteRuntimeEngineFactory;

/**
 * 
 * Start a process using JMS.
 * 
 * @author wsiqueir
 * 
 */
public class StartProcessJMSTest {

	private static final String CTX_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
	private static final String DEPLOYMENT_ID = "example:proj_stuck:1.0";
	private static final String PROCESS_ID = "proj_stuck.stuck_proc";
	private static final String USER = "jesuino";
	private static final String PASSWORD = "redhat2014!";
	private static final String JMS_URL = "remote://localhost:4447";

	private RuntimeEngine engine;

	@Before
	public void initiliazeEngine() throws NamingException {
		engine = RemoteRuntimeEngineFactory.newJmsBuilder()
				.addDeploymentId(DEPLOYMENT_ID).addUserName(USER)
				.addPassword(PASSWORD)
				.addRemoteInitialContext(createInitialContext()).build();
	}

	@Test
	public void start() {
		KieSession kieSession = engine.getKieSession();
		ProcessInstance pi = kieSession.startProcess(PROCESS_ID);
		System.out.println("Process Started! Instance ID: " + pi.getId());
	}

	static private InitialContext createInitialContext() throws NamingException {
		Properties props = new Properties();
		props.setProperty(InitialContext.INITIAL_CONTEXT_FACTORY, CTX_FACTORY);
		props.setProperty(InitialContext.PROVIDER_URL, JMS_URL);
		props.setProperty(InitialContext.SECURITY_PRINCIPAL, USER);
		props.setProperty(InitialContext.SECURITY_CREDENTIALS, PASSWORD);
		return new InitialContext(props);
	}
}