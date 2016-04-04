package org.fxapps.bpms.remote;

import java.net.MalformedURLException;
import java.net.URL;

import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.remote.client.api.RemoteRuntimeEngineFactory;

public abstract class RemoteAPITestBase {

	protected static final String DEPLOYMENT_ID = "test:proj1:1.0";
	protected static final String APP_URL = "http://localhost:8080/business-central";
	protected static final String USER = "jesuino";
	protected static final String PASSWORD = "redhat2014!";
	protected static final int TIMEOUT = 2000;
	protected RuntimeEngine engine;

	public RemoteAPITestBase() {
		long m = System.currentTimeMillis();
		try {
			engine = RemoteRuntimeEngineFactory.newRestBuilder()
					.addUrl(new URL(APP_URL)).addUserName(USER)
					.addProcessInstanceId(10l).addPassword(PASSWORD)
					.addDeploymentId(DEPLOYMENT_ID).addTimeout(TIMEOUT).build();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		System.out.println("Time to create the remote engine :"
				+ (System.currentTimeMillis() - m));
	}

	public abstract void doTest() throws Exception;

}
