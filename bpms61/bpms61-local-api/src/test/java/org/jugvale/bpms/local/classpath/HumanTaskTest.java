package org.jugvale.bpms.local.classpath;

import java.util.Properties;

import org.jbpm.runtime.manager.impl.RuntimeEnvironmentBuilder;
import org.jbpm.runtime.manager.impl.RuntimeManagerFactoryImpl;
import org.jbpm.services.task.identity.JBossUserGroupCallbackImpl;
import org.jbpm.test.JBPMHelper;
import org.junit.Test;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.task.TaskService;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.manager.RuntimeEnvironment;
import org.kie.internal.runtime.manager.context.EmptyContext;
import org.kie.internal.task.api.UserGroupCallback;

/**
 * 
 * Based on human task example from jbpm-examples project
 * @author wsiqueir
 *
 */
public class HumanTaskTest {

	private static final String GROUP = "group";
	private static final String USER = "user";

	@Test
    public void doTest() {
        try {
            RuntimeManager manager = getRuntimeManager("org/jugvale/bpms/local/processes/humantask/HumanTask.bpmn");        
            RuntimeEngine runtime = manager.getRuntimeEngine(EmptyContext.get());
            KieSession ksession = runtime.getKieSession();
            ksession.startProcess("test.proc_ht");

            // "sales-rep" reviews request
            TaskService taskService = runtime.getTaskService();
    		long taskId = taskService.getTasksAssignedAsPotentialOwner(USER, "en-UK").get(0).getId();
    		taskService.claim(taskId, USER);
    		taskService.start(taskId, USER);
    		taskService.complete(taskId, USER, null);
    		System.out.println("Process instance completed");
    		manager.disposeRuntimeEngine(runtime);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        System.exit(0);
    }

    private RuntimeManager getRuntimeManager(String process) {
        // load up the knowledge base
    	JBPMHelper.startH2Server();
    	JBPMHelper.setupDataSource();
    	Properties properties= new Properties();
        properties.setProperty(USER, GROUP);
        UserGroupCallback userGroupCallback = new JBossUserGroupCallbackImpl(properties);
        RuntimeEnvironment environment = RuntimeEnvironmentBuilder.getDefault()
            .userGroupCallback(userGroupCallback)
            .addAsset(ResourceFactory.newClassPathResource(process), ResourceType.BPMN2)
            .get();
        RuntimeManagerFactoryImpl factory = new RuntimeManagerFactoryImpl();
        return factory.newSingletonRuntimeManager(environment);
    }
    
}
