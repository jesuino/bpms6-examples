package com.redhat.gss.bpms.data;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.task.TaskService;
import org.kie.internal.runtime.manager.context.EmptyContext;

import com.redhat.gss.bpms.Util;

public class HTProcessRunner {

	public static void main(String[] args) throws Exception {
        RuntimeManager manager = Util.getRuntimeManager();
        RuntimeEngine engine = manager.getRuntimeEngine(EmptyContext.get());
        KieSession kSession = engine.getKieSession();
        TaskService taskService = engine.getTaskService();
		Long piid = kSession.startProcess("com.redhat.gss.bpms.HT").getId();
		Long tid = taskService.getTasksByProcessInstanceId(piid).iterator().next();
		taskService.start(tid, "bpmsAdmin");
		taskService.complete(tid, "bpmsAdmin", null);
		manager.disposeRuntimeEngine(engine);
		manager.close();
	}

	


	
}
