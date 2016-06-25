package org.fxapps.bpms.local.classpath;

import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.fxapps.bpms.model.Person;
import org.jbpm.test.JBPMHelper;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.persistence.jpa.KieStoreServices;
import org.kie.api.runtime.Environment;
import org.kie.api.runtime.EnvironmentName;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.KnowledgeBaseFactory;

import bitronix.tm.TransactionManagerServices;

public class WorkingMemoryPersistenceTest {

	private static final boolean INSERT_DATA = false;
	private static final Long PREVIOUS_ID = 2l;
	Logger logger = Logger.getLogger(getClass().getName());

	@Test
	public void doTest() throws Exception {
		logger.info("Running test using persistence in the worki");
		// start our mock DB
		JBPMHelper.startH2Server();
		JBPMHelper.setupDataSource();
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("org.jbpm.persistence.jpa");
		Environment env = KnowledgeBaseFactory.newEnvironment();
		TransactionManager txManager = TransactionManagerServices
				.getTransactionManager();

		KieServices kieServices = KieServices.Factory.get();
		env.set(EnvironmentName.ENTITY_MANAGER_FACTORY, emf);
		env.set(EnvironmentName.TRANSACTION_MANAGER, txManager);
		KieContainer kc = KieServices.Factory.get().getKieClasspathContainer();
		KieBase kb = kc.getKieBase("workingmemorypersistencetest");
		KieStoreServices kStore = kieServices.getStoreServices();
		UserTransaction ut = (UserTransaction) new InitialContext()
				.lookup("java:comp/UserTransaction");
		if (INSERT_DATA) {
			ut.begin();
			KieSession ksession = kStore.newKieSession(kb, null, env);
			long sessionIdentifier = ksession.getIdentifier();
			System.out.println(">>> SESSION ID IS: " + sessionIdentifier
					+ " - SAVE IT TO USE LATER. <<<");
			ksession.insert(new Person(20, "John"));
			ksession.insert(new Person(15, "Marc"));
			ksession.insert(new Person(30, "Mary"));
			ksession.insert(new Person(12, "Brad"));
			ksession.insert(new Person(17, "Deb"));
			ut.commit();
		} else {
			ut.begin();
			System.out.println(">>> fireAllRules SESSION " + PREVIOUS_ID
					+ " <<<");
			KieSession ksession = kStore.loadKieSession(PREVIOUS_ID, kb, null,
					env);
			ksession.fireAllRules();
			ut.commit();
		}
	}
}