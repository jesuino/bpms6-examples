package org.fxapps.bpms.remote.api.tests;

import org.fxapps.bpms.remote.RemoteAPITestBase;
import org.junit.Test;
import org.kie.api.runtime.KieSession;

public class StartProcessTest extends RemoteAPITestBase {

	private static final String PROCESS_ID = "proc_migration.prc_migration";

	@Test
	public void doTest() throws Exception {
		KieSession kSession = engine.getKieSession();
		long piid = kSession.startProcess(PROCESS_ID).getId();
		System.out.println(piid);
	}
}
