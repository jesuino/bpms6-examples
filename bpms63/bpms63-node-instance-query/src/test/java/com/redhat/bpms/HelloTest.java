package com.redhat.bpms;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.jbpm.kie.services.impl.RuntimeDataServiceImpl;
import org.jbpm.shared.services.impl.TransactionalCommandService;
import org.jbpm.test.JBPMHelper;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeEnvironment;
import org.kie.api.runtime.manager.RuntimeEnvironmentBuilder;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.manager.RuntimeManagerFactory;
import org.kie.api.task.UserGroupCallback;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.manager.context.EmptyContext;

/**
 * 
 * Just a very simple example of an unit test setup (comment persistence.xml )
 * 
 * @author wsiqueir
 *
 */
public class HelloTest {
	
	private static RuntimeManager runtimeManager;
	private static RuntimeDataServiceImpl services;

	@BeforeClass
	public static void before() {
		JBPMHelper.startH2Server();
		JBPMHelper.setupDataSource();
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("org.jbpm.persistence.jpa");

		TransactionalCommandService commandService = new TransactionalCommandService(
				emf);
		services = new RuntimeDataServiceImpl();
		services.setCommandService(commandService);

		UserGroupCallback userGroupCallback = new UserGroupCallback() {
			
			@Override
			public List<String> getGroupsForUser(String userId, List<String> groupIds,
					List<String> allExistingGroupIds) {
				return Collections.emptyList();
			}
			
			@Override
			public boolean existsUser(String userId) {
				return true;
			}
			@Override
			public boolean existsGroup(String groupId) {
				return true;
			}
		};

		RuntimeEnvironment builder = RuntimeEnvironmentBuilder.Factory
				.get().newDefaultBuilder().entityManagerFactory(emf)
				.userGroupCallback(userGroupCallback).addAsset(
						ResourceFactory
						.newClassPathResource("helloProcess.bpmn2"),
				ResourceType.BPMN2)
		.addAsset(ResourceFactory.newClassPathResource("HT.bpmn2"),
				ResourceType.BPMN2).get();;

		 runtimeManager = RuntimeManagerFactory.Factory.get().newPerRequestRuntimeManager(
				builder, "com.redhat.gss:test:1.0");
		 
	}
	
	@Test
	public void doTest() {
		RuntimeEngine runtimeEngine = runtimeManager.getRuntimeEngine(EmptyContext.get());
		runtimeEngine.getKieSession().startProcess("com.redhat.gss.bpms.helloProcess");
		runtimeManager.close();
	}

}
