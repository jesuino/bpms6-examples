package org.fxapps.bpms.remote.api.tests;

import org.fxapps.bpms.remote.RemoteAPITestBase;
import org.junit.Test;
import org.kie.api.runtime.KieSession;

public class StartAndSignalTest extends RemoteAPITestBase {

	private static final String PARAMETER = "PARAMETER PASSED";
	private static final String SIGNAL = "SIGNAL_REMOTE";
	private static final String PROCESS_ID = "project1.proc_signal";

	@Test
	public void doTest() throws Exception {
		KieSession kSession = engine.getKieSession();
		long piid = kSession.startProcess(PROCESS_ID).getId();
		kSession.signalEvent(SIGNAL, PARAMETER, piid);
	}
}