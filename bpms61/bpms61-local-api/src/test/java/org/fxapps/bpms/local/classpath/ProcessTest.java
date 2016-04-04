package org.fxapps.bpms.local.classpath;

import java.util.logging.Logger;

import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class ProcessTest {
	
	Logger logger = Logger.getLogger(getClass().getName());


	@Test
	public void doTest() {
		logger.info("Testing simple process without persistence");
		KieContainer kc = KieServices.Factory.get().getKieClasspathContainer();
		KieSession ksession = kc.getKieBase("TestProcessKB").newKieSession();
		ksession.startProcess("HelloWorldProcess");
		ksession.dispose();
	}

}