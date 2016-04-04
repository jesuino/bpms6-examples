package org.jugvale.bpms.local.classpath;

import java.util.logging.Logger;

import org.junit.Test;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.conf.RuleEngineOption;

public class RuleWithFunctionTest {
	Logger logger = Logger.getLogger(getClass().getName());

	@Test
	public void doTest() {
		System.setProperty("drools.ruleEngine", "reteoo");
		logger.info("Using a function in the LHS");
		KieContainer kc = KieServices.Factory.get().getKieClasspathContainer();
		KieSession ksession = kc.getKieBase("TestRulesFunctionKB").newKieSession();
		ksession.fireAllRules();
		ksession.dispose();
	}

}