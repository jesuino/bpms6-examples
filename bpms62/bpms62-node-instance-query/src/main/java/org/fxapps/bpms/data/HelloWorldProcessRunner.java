package org.fxapps.bpms.data;

import org.fxapps.bpms.Util;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.internal.runtime.manager.context.EmptyContext;

public class HelloWorldProcessRunner {

	public static void main(String[] args) throws Exception {
        RuntimeManager manager = Util.getRuntimeManager();
        RuntimeEngine engine = manager.getRuntimeEngine(EmptyContext.get());
        KieSession kSession = engine.getKieSession();
		for (int i = 0; i < 50000; i++) {
			System.out.println("Starting process number " + i);
			kSession.startProcess("org.fxapps.bpms.helloProcess");
		}
		manager.disposeRuntimeEngine(engine);
		manager.close();
	}
	
}
