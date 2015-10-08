package org.jugvale.bpms.local.bc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.drools.core.io.impl.UrlResource;
import org.jbpm.runtime.manager.impl.RuntimeEnvironmentBuilder;
import org.jbpm.runtime.manager.impl.RuntimeManagerFactoryImpl;
import org.jbpm.services.task.identity.JBossUserGroupCallbackImpl;
import org.jbpm.test.JBPMHelper;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.task.TaskService;
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

	private static final String TASK_GROUP = "group";
	private static final String TASK_USER = "user";
	
	// information about the artifact from BC to load
	private static final String PASSWORD = "redhat2014!";
	private static final String USER = "jesuino";
	private static final String URL = "http://localhost:8080/business-central/maven2/org/kie/example/project1/1.0/project1-1.0.jar";

	@Test
    public void doTest() {
        try {
        	KieBase kbase = getKieBase(URL, USER, PASSWORD);
            RuntimeManager manager = getRuntimeManager(kbase);        
            RuntimeEngine runtime = manager.getRuntimeEngine(EmptyContext.get());
            KieSession ksession = runtime.getKieSession();
            ksession.startProcess("test.proc_ht");

            // "sales-rep" reviews request
            TaskService taskService = runtime.getTaskService();
    		long taskId = taskService.getTasksAssignedAsPotentialOwner(TASK_USER, "en-UK").get(0).getId();
    		taskService.claim(taskId, TASK_USER);
    		taskService.start(taskId, TASK_USER);
    		taskService.complete(taskId, TASK_USER, null);
    		System.out.println("Process instance completed");
    		manager.disposeRuntimeEngine(runtime);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        System.exit(0);
    }
	
	private KieBase getKieBase(String url, String usr, String password) throws IOException {
		KieServices ks = KieServices.Factory.get();
		KieRepository kr = ks.getRepository();
		UrlResource urlResource = (UrlResource) ks.getResources().newUrlResource(URL);
		urlResource.setUsername(USER);
		urlResource.setPassword(PASSWORD);
		urlResource.setBasicAuthentication("enabled");
		InputStream is = urlResource.getInputStream();
		KieModule kModule = kr.addKieModule(ks.getResources().newInputStreamResource(is));
		KieContainer kc = ks.newKieContainer(kModule.getReleaseId());
		return kc.getKieBase();		
	}

    private RuntimeManager getRuntimeManager(KieBase kbase) {
        // load up the knowledge base
    	JBPMHelper.startH2Server();
    	JBPMHelper.setupDataSource();
    	Properties properties= new Properties();
        properties.setProperty(TASK_USER, TASK_GROUP);
        UserGroupCallback userGroupCallback = new JBossUserGroupCallbackImpl(properties);
        RuntimeEnvironment environment = RuntimeEnvironmentBuilder.getDefault()
            .userGroupCallback(userGroupCallback)
            .knowledgeBase(kbase)
            .get();
        RuntimeManagerFactoryImpl factory = new RuntimeManagerFactoryImpl();
        return factory.newSingletonRuntimeManager(environment);
    }
    
}
