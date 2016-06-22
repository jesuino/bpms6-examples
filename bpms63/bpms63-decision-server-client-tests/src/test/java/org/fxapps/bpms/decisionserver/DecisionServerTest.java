package org.fxapps.bpms.decisionserver;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.api.runtime.ExecutionResults;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.model.KieContainerResource;
import org.kie.server.api.model.KieContainerResourceList;
import org.kie.server.api.model.KieServerInfo;
import org.kie.server.api.model.ReleaseId;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.api.model.ServiceResponse.ResponseType;
import org.kie.server.api.model.definition.ProcessDefinition;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.QueryServicesClient;
import org.kie.server.client.RuleServicesClient;



/**
 * @author wsiqueir
 * 
 */
public class DecisionServerTest {

	/**
	 * Decision Server URL
	 */
	private static final String URL = "http://localhost:8180/kie-server/services/rest/server";
	/**
	 * Decision Server User in role "kie-server"
	 */
	private static final String USER = "kieserver";
	/**
	 * User's password
	 */
	private static final String PASSWORD = "kieserver1!";
	/**
	 * The data format used in the payload of the requests
	 */
	private static final MarshallingFormat FORMAT = MarshallingFormat.JSON;
	/**
	 * A simple container that contains BRMS rules
	 */
	private static final String RULES_CONTAINER = "hello";
	
	
	/**
	 * A simple container will be created using the GAV information below 
	 */
	private static final String GROUP = "example";
	private static final String ARTIFACT_ID = "hello_rule";
	private static final String VERSION = "1.0";

	private KieServicesConfiguration conf;
	private KieServicesClient kieServicesClient;


	
	@Before
	public void initialize() {
		conf = KieServicesFactory.newRestConfiguration(URL, USER, PASSWORD);
		conf.setMarshallingFormat(FORMAT);
		kieServicesClient = KieServicesFactory.newKieServicesClient(conf);
	}

	@Test
	public void listCapabilities() {
		ServiceResponse<KieServerInfo> r = kieServicesClient.getServerInfo();
		r.getType();
		KieServerInfo serverInfo = kieServicesClient.getServerInfo()
				.getResult();
		System.out.println("== Server capabilities: ==");
		for (String capability : serverInfo.getCapabilities()) {
			System.out.print(" " + capability);
		}
		System.out.println();
	}

	@Test
	public void listContainers() {
		KieContainerResourceList containersList = kieServicesClient
				.listContainers().getResult();
		List<KieContainerResource> kieContainers = containersList
				.getContainers();
		System.out.println("== Available containers: ==");
		for (KieContainerResource container : kieContainers) {
			System.out.println("\t" + container.getContainerId() + " ("
					+ container.getReleaseId() + ")");
		}
	}
	
	@Test
	public void createSimpleContainer() {
		System.out.println("== CREATE A TEST CONTAINER ==");
		List<KieContainerResource> kieContainers = kieServicesClient
				.listContainers().getResult().getContainers();
		KieContainerResource container = null;		
		for (KieContainerResource kieContainerResource : kieContainers) {
			if(kieContainerResource.getContainerId().equals(RULES_CONTAINER)){
				System.out.println("Found rules container. No new container will be created.");
				container = kieContainerResource;
			}
		}
		if(container == null) {
			System.out.print("Container " + RULES_CONTAINER + " not found. Creating it...");
			System.out.println("(make sure you installed hello_rule-1.0.jar in your repository)...");
			ReleaseId releaseId = new ReleaseId(GROUP, ARTIFACT_ID, VERSION);
			container = new KieContainerResource(RULES_CONTAINER, releaseId);
		}
		String containerId = container.getContainerId();
		ServiceResponse<Void> responseDispose = kieServicesClient
				.disposeContainer(containerId);
		if (responseDispose.getType() == ResponseType.FAILURE) {
			System.out.println("Error disposing " + containerId + ". Message: ");
			System.out.println(responseDispose.getMsg());
			return;
		}
		ServiceResponse<KieContainerResource> createResponse = kieServicesClient
				.createContainer(containerId, container);
		if (createResponse.getType() == ResponseType.FAILURE) {
			System.out.println("Error creating " + containerId + ". Message: ");
			System.out.println(responseDispose.getMsg());
			return;
		}
		System.out.println("Container create with success!");	
	}

	@Test
	public void disposeAndCreateContainer() {
		System.out.println("== Disposing and creating containers ==");
		List<KieContainerResource> kieContainers = kieServicesClient
				.listContainers().getResult().getContainers();
		if (kieContainers.size() == 0) {
			System.out.println("No containers available...");
			return;
		}
		KieContainerResource container = kieContainers.get(0);
		String containerId = container.getContainerId();
		ServiceResponse<Void> responseDispose = kieServicesClient
				.disposeContainer(containerId);
		if (responseDispose.getType() == ResponseType.FAILURE) {
			System.out
					.println("Error disposing " + containerId + ". Message: ");
			System.out.println(responseDispose.getMsg());
			return;
		}
		System.out.println("Success Disposing container " + containerId);
		System.out.println("Trying to recreate the container...");
		ServiceResponse<KieContainerResource> createResponse = kieServicesClient
				.createContainer(containerId, container);
		if (createResponse.getType() == ResponseType.FAILURE) {
			System.out.println("Error creating " + containerId + ". Message: ");
			System.out.println(responseDispose.getMsg());
			return;
		}
		System.out.println("Container recreated with success!");
	}

	@Test
	public void executeCommands() {
		System.out.println("== Sending commands to the server ==");
		RuleServicesClient rulesClient = kieServicesClient
				.getServicesClient(RuleServicesClient.class);
		KieCommands commandsFactory = KieServices.Factory.get().getCommands();
		Command<?> insert = commandsFactory.newInsert("Some String OBJ");
		Command<?> fireAllRules = commandsFactory.newFireAllRules();
		Command<?> batchCommand = commandsFactory.newBatchExecution(Arrays
				.asList(insert, fireAllRules));
		ServiceResponse<ExecutionResults> executeResponse = rulesClient.executeCommandsWithResults(RULES_CONTAINER, batchCommand);
		if (executeResponse.getType() == ResponseType.SUCCESS) {
			System.out.println("Commands executed with success! Response: ");
			System.out.println(executeResponse.getResult());
		} else {
			System.out.println("Error executing rules. Message: ");
			System.out.println(executeResponse.getMsg());
		}
	}

	@Test
	public void listProcesses() {
		System.out.println("== Listing Business Processes ==");
		QueryServicesClient queryClient = kieServicesClient
				.getServicesClient(QueryServicesClient.class);
		List<ProcessDefinition> findProcessesByContainerId = queryClient
				.findProcessesByContainerId("rewards", 0, 1000);
		for (ProcessDefinition def : findProcessesByContainerId) {
			System.out.println(def.getName() + " - " + def.getId() + " v"
					+ def.getVersion());
		}
	}

}