package org.jugvale.bpms.local.bc;

import java.io.IOException;
import java.io.InputStream;

import org.drools.core.io.impl.UrlResource;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

/**
 * 
 * Loads a specific artifact from business central and start a process
 * 
 * @author wsiqueir
 * 
 */
public class UrlResourceTest {

	private static final String PASSWORD = "redhat2014!";
	private static final String USER = "jesuino";
	private static final String URL = "http://localhost:8080/business-central/maven2/org/kie/example/project1/1.0/project1-1.0.jar";

	@Test
	public void doTest() throws IOException {
		KieServices ks = KieServices.Factory.get();
		KieRepository kr = ks.getRepository();
		UrlResource urlResource = (UrlResource) ks.getResources().newUrlResource(URL);
		urlResource.setUsername(USER);
		urlResource.setPassword(PASSWORD);
		urlResource.setBasicAuthentication("enabled");
		InputStream is = urlResource.getInputStream();
		KieModule kModule = kr.addKieModule(ks.getResources().newInputStreamResource(is));
		KieContainer kc = ks.newKieContainer(kModule.getReleaseId());
		KieSession kSession = kc.newKieSession();
		kSession.startProcess("project1.hello");
	}

}
