package org.fxapps.bpms.local.classpath;

import org.fxapps.bpms.model.Person;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class ApprovePersonRuleTest {
	
	@Test
	public void approvePerson() {
		System.out.println("APPROVE PERSON TEST");
		KieContainer kc = KieServices.Factory.get().getKieClasspathContainer();
		KieBase kb = kc.getKieBase("workingmemorypersistencetest");		
		KieSession ksession = kb.newKieSession();
		ksession.insert(new Person(20, "John"));
		ksession.insert(new Person(15, "Marc"));
		ksession.insert(new Person(30, "Mary"));
		ksession.insert(new Person(12, "Brad"));
		ksession.insert(new Person(17, "Deb"));
		ksession.fireAllRules();
		ksession.dispose();
	}
}
