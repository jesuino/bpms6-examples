package org.fxapps.bpms.remote.api.tests;

import org.fxapps.bpms.remote.RemoteAPITestBase;
import org.junit.Test;

public class RuntimeManagerCreationTest extends RemoteAPITestBase {

	@Override
	@Test
	public void doTest() throws Exception {
		engine.getTaskService().getTaskById(1);
		

	}

}
