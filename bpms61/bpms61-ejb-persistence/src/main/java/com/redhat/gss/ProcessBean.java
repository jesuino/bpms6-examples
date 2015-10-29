package com.redhat.gss;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeEnvironment;
import org.kie.api.runtime.manager.RuntimeEnvironmentBuilder;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.manager.RuntimeManagerFactory;

@Singleton
public class ProcessBean {

	private static final String PROCESS_INSTANCEID = "HelloWorldProcess";

	private static final String KBNAME = "TestProcessKB";

	private static final String DEPLOYMENT_ID = "com.redhat.gss:test:1.0";

	@PersistenceUnit(unitName = "com.redhat.gss.domain")
	private EntityManagerFactory emf;

	private RuntimeManager manager;

	public void startProcess() {
		RuntimeEngine engine = manager.getRuntimeEngine(null);
		KieSession ksession = engine.getKieSession();
		ksession.startProcess(PROCESS_INSTANCEID);
	}

	@PostConstruct
	public void initializeRuntimeManager() {
		KieServices ks = KieServices.Factory.get();
		KieContainer kContainer = ks.getKieClasspathContainer();
		KieBase kbase = kContainer.getKieBase(KBNAME);
		manager = createRuntimeManager(kbase);
	}

	private RuntimeManager createRuntimeManager(KieBase kbase) {
		RuntimeEnvironmentBuilder builder = RuntimeEnvironmentBuilder.Factory
				.get().newDefaultBuilder().entityManagerFactory(emf)
				.knowledgeBase(kbase);
		RuntimeEnvironment env = builder.get();
		RuntimeManagerFactory man = RuntimeManagerFactory.Factory.get();
		RuntimeManager m = man.newPerRequestRuntimeManager(env, DEPLOYMENT_ID);
		return m;
	}

}