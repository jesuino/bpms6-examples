package org.fxapps.bpms.remote.api.tests;

import java.net.MalformedURLException;

import org.fxapps.bpms.remote.RemoteAPITestBase;
import org.junit.Test;

public class TaskServicePerformanceTest extends RemoteAPITestBase {

	@Test
	public void doTest() throws MalformedURLException {
		int i = 10;
		while (i-- != 0) {
			long t = System.currentTimeMillis();
			engine.getTaskService();
			System.out.println("time in ms: "
					+ (System.currentTimeMillis() - t));
		}
	}

}
