package com.redhat.gss.bpms.data;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.internal.runtime.manager.context.EmptyContext;

import com.redhat.gss.bpms.Util;

public class HelloWorldProcessRunner {

	public static void main(String[] args) throws Exception {
        RuntimeManager manager = Util.getRuntimeManager();
        RuntimeEngine engine = manager.getRuntimeEngine(EmptyContext.get());
        KieSession kSession = engine.getKieSession();
		for (int i = 0; i < 100000; i++) {
			System.out.println("Starting process number " + i);
			kSession.startProcess("com.redhat.gss.bpms.helloProcess");
		}
		manager.disposeRuntimeEngine(engine);
		manager.close();
	}

	


	
}
