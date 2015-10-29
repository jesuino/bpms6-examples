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
 * Fire all rules of a kjar created in BRMS and available in its maven repository. <br />
 * If the BRMS is in a remote server, make sure to update your maven configuration to point to its repository. <br />
 * To have access to the facts or events definition present on this kjar declare a dependency to this module in your pom.xml
 * @author wsiqueir
 *
 */
public class MavenArtifactTest {
	
	// We could instead load the URL use the GAV of the artifact and use KieScanner to load it
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
		// This fact was created using Data Modeller in business central
	//	Product p = new Product();
	//	p.setName("Table");
	//	p.setPrice(500f);
		KieSession kSession = kc.newKieSession();
	//	kSession.insert(p);
		kSession.fireAllRules();
	}

}
