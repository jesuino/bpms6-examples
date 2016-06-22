package org.fxapps.bpms;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.kie.api.io.ResourceType;
import org.kie.api.runtime.manager.RuntimeEnvironment;
import org.kie.api.runtime.manager.RuntimeEnvironmentBuilder;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.manager.RuntimeManagerFactory;
import org.kie.api.task.UserGroupCallback;
import org.kie.internal.io.ResourceFactory;

import bitronix.tm.resource.jdbc.PoolingDataSource;

/**
 * @author wsiqueir
 *
 */
public class Util {

	private static final String DB_NAME = "bpms63-node-query";
	private static final String PASSWORD = "";
	private static final String USER = "root";
	private static final String URL = "jdbc:mysql://localhost:3306/bpms63-node-query";
	private static final String DS_NAME = "node-process-test";

	/**
	 * Configure a datasource for our engine use. It is configured for MySQL
	 */
	private static void setupPoolingDataSource() {
		PoolingDataSource pds = new PoolingDataSource();
		pds.setMaxPoolSize(10);
		pds.setMinPoolSize(10);
		pds.setUniqueName(DS_NAME);
		pds.setClassName("com.mysql.jdbc.jdbc2.optional.MysqlXADataSource");
		pds.setAllowLocalTransactions(true);
		pds.getDriverProperties().put("user", USER);
		pds.getDriverProperties().put("password", PASSWORD);
		pds.getDriverProperties().put("url", URL);
		pds.getDriverProperties().put("databaseName", DB_NAME);
		pds.init();
	}

	/**
	 * 
	 * Create the runtime manager for the process in the classpath
	 * 
	 * @return
	 */
	public static RuntimeManager getRuntimeManager() {
		setupPoolingDataSource();
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("org.jbpm.persistence.jpa");
		RuntimeEnvironment env = RuntimeEnvironmentBuilder.Factory
				.get()
				.newDefaultBuilder()
				.userGroupCallback(new UserGroupCallback() {

					@Override
					public List<String> getGroupsForUser(String userId,
							List<String> groupIds,
							List<String> allExistingGroupIds) {
						return null;
					}

					@Override
					public boolean existsUser(String userId) {
						return true;
					}

					@Override
					public boolean existsGroup(String groupId) {
						return true;
					}
				})
				.entityManagerFactory(emf)
				.addAsset(
						ResourceFactory
								.newClassPathResource("helloProcess.bpmn2"),
						ResourceType.BPMN2)
				.addAsset(ResourceFactory.newClassPathResource("HT.bpmn2"),
						ResourceType.BPMN2).get();
		RuntimeManager manager = RuntimeManagerFactory.Factory.get()
				.newSingletonRuntimeManager(env);
		return manager;
	}

}
