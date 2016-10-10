package org.fxapps.bpms;

import org.drools.compiler.kproject.ReleaseIdImpl;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class Main {

	final static String G = "example";
	final static String A = "testvini";
	final static String V = "1.0";

	public static void main(String[] args) {
		ReleaseIdImpl releaseId = new ReleaseIdImpl(G, A, V);
		KieContainer kContainer = KieServices.Factory.get().newKieContainer(releaseId);
		KieSession kSession = kContainer.newKieSession();
		kSession.insert("William");
		kSession.fireAllRules();
	}

}
