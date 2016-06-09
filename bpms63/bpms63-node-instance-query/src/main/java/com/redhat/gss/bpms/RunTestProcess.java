package com.redhat.gss.bpms;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeEnvironment;
import org.kie.api.runtime.manager.RuntimeEnvironmentBuilder;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.manager.RuntimeManagerFactory;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.manager.context.EmptyContext;

import bitronix.tm.resource.jdbc.PoolingDataSource;

public class RunTestProcess {

	public static void main(String[] args) throws Exception {
		setupPoolingDataSource();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("org.jbpm.persistence.jpa");
        RuntimeEnvironment env = 
             RuntimeEnvironmentBuilder.Factory.get().newDefaultBuilder()
             .entityManagerFactory(emf) 
             .addAsset(ResourceFactory.newClassPathResource("helloProcess.bpmn2"),ResourceType.BPMN2)
             .get(); 
        RuntimeManager manager = RuntimeManagerFactory.Factory.get().newSingletonRuntimeManager(env);
        RuntimeEngine engine = manager.getRuntimeEngine(EmptyContext.get());
        KieSession kSession = engine.getKieSession();
		for (int i = 0; i < 100000; i++) {
			System.out.println("Starting process number " + i);
			kSession.startProcess("com.redhat.gss.bpms.helloProcess");
		}
		manager.disposeRuntimeEngine(engine);
		manager.close();
	}

	public static PoolingDataSource setupPoolingDataSource() {
		PoolingDataSource pds = new PoolingDataSource();
		pds.setMaxPoolSize(10);
		pds.setMinPoolSize(10);
		pds.setUniqueName("node-process-test");
		pds.setClassName("com.mysql.jdbc.jdbc2.optional.MysqlXADataSource");
		pds.setAllowLocalTransactions(true);
		pds.getDriverProperties().put("user", "root");
		pds.getDriverProperties().put("url", "jdbc:mysql://localhost:3306/bpms63-node-query");
		pds.getDriverProperties().put("databaseName", "bpms63-node-query");
		pds.init();
		return pds;
	}
	
}
