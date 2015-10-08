package org.jugvale.bpms.local.classpath;

import java.util.logging.Logger;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.jbpm.test.JBPMHelper;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.runtime.Environment;
import org.kie.api.runtime.EnvironmentName;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.persistence.jpa.JPAKnowledgeService;
import org.kie.internal.runtime.StatefulKnowledgeSession;

public class ProcessPersistenceTest {
	
	Logger logger = Logger.getLogger(getClass().getName());
	

	@Test
	public void doTest() {
		logger.info("Running test using persistence");
		// start our mock DB
		JBPMHelper.startH2Server();
		JBPMHelper.setupDataSource();
		// setting persistence
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("org.jbpm.persistence.jpa");
		Environment env = KnowledgeBaseFactory.newEnvironment();
		// to load our kbase
		KieContainer kc = KieServices.Factory.get().getKieClasspathContainer();
		KieBase kb = kc.getKieBase("TestProcessKB");
		env.set(EnvironmentName.ENTITY_MANAGER_FACTORY, emf);
		StatefulKnowledgeSession kSession = JPAKnowledgeService
				.newStatefulKnowledgeSession(kb, null, env);
		kSession.startProcess("HelloWorldProcess");
	}
}