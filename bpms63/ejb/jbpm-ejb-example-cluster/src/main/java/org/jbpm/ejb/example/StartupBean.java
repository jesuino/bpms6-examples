package org.jbpm.ejb.example;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class StartupBean {

	@PostConstruct
	public void init() {
		System.setProperty("org.jbpm.ht.callback", "jaas");
	}
}
