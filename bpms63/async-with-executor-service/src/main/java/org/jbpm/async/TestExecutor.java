package org.jbpm.async;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.jbpm.bpmn2.handler.ServiceTaskHandler;
import org.jbpm.executor.ExecutorServiceFactory;
import org.jbpm.runtime.manager.impl.RuntimeEnvironmentBuilder;
import org.jbpm.test.JBPMHelper;
import org.kie.api.KieServices;
import org.kie.api.executor.ExecutorService;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeEnvironment;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.manager.RuntimeManagerFactory;
import org.kie.internal.runtime.manager.context.EmptyContext;

public class TestExecutor {

	public static void main(String[] args) throws InterruptedException {
		
		// no delay to call the executor and not using JMS
		System.setProperty("org.kie.executor.jms", "false");
		System.setProperty("org.kie.executor.initial.delay", "1");
		
		JBPMHelper.startH2Server();
		JBPMHelper.setupDataSource();
		
		// This entity manager factory contains Executor entities
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("org.jbpm.persistence.with.executor");
		ExecutorService executorService = ExecutorServiceFactory.newExecutorService();
		
		// initialize the executor service - it is important or it won't work
		executorService.init();
		
		
		RuntimeEnvironment env = RuntimeEnvironmentBuilder.Factory
				.get()
				.newEmptyBuilder()
				// remember to register the executor service
				.addEnvironmentEntry("ExecutorService", executorService) 
				.entityManagerFactory(emf)
				.addAsset(
						KieServices.Factory.get().getResources()
								.newClassPathResource("org.jbpm.async_test.v1.0.bpmn2"),
						ResourceType.BPMN2).get();
		

		RuntimeManager runtimeManager = RuntimeManagerFactory.Factory.get().newSingletonRuntimeManager(env);
		RuntimeEngine engine = runtimeManager.getRuntimeEngine(EmptyContext.get());
		KieSession kieSession = engine.getKieSession();
		
		// registering the WIH for the Service task
		kieSession.getWorkItemManager().registerWorkItemHandler("Service Task", new ServiceTaskHandler());

		kieSession.startProcess("test.async_test");

		runtimeManager.disposeRuntimeEngine(engine);
	}

}
