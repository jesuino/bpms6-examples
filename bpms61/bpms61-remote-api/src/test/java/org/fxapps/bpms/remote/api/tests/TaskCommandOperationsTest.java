package org.fxapps.bpms.remote.api.tests;

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.codec.binary.Base64;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientRequestFactory;
import org.jboss.resteasy.client.ClientResponse;
import org.junit.Test;
import org.kie.api.command.Command;
import org.kie.remote.client.jaxb.JaxbCommandsRequest;
import org.kie.remote.client.jaxb.JaxbCommandsResponse;
import org.kie.remote.jaxb.gen.ClaimTaskCommand;
import org.kie.remote.jaxb.gen.CompleteTaskCommand;
import org.kie.remote.jaxb.gen.StartTaskCommand;
import org.kie.services.client.serialization.JaxbSerializationProvider;
import org.kie.services.client.serialization.jaxb.impl.JaxbCommandResponse;

/**
 * 
 * A simple class to show how to claim, start and complete a task
 * 
 * @author wsiqueir
 * 
 */
public class TaskCommandOperationsTest {

	protected static final String DEPLOYMENT_ID = "org.kie.example:project1:1.0.0-SNAPSHOT";
	private static final String APP_URL = "http://localhost:8080/business-central";
	private static final String USER = "jesuino";
	private static final String PASSWORD = "redhat2014!";

	final long TASK_ID = 8l;

	@SuppressWarnings("rawtypes")
	@Test
	public void doTest() throws Exception {
		ClaimTaskCommand claimTaskCmd = new ClaimTaskCommand();
		claimTaskCmd.setTaskId(TASK_ID);
		claimTaskCmd.setUserId(USER);
		StartTaskCommand startTaskCmd = new StartTaskCommand();
		startTaskCmd.setTaskId(TASK_ID);
		startTaskCmd.setUserId(USER);
		CompleteTaskCommand completeTaskCommand = new CompleteTaskCommand();
		completeTaskCommand.setTaskId(TASK_ID);
		completeTaskCommand.setUserId(USER);
		List<Command> cmds = new ArrayList<>();
		cmds.add(claimTaskCmd);
		cmds.add(startTaskCmd);
		cmds.add(completeTaskCommand);
		executeCommand(cmds);
	}

	@SuppressWarnings("rawtypes")
	private List<JaxbCommandResponse<?>> executeCommand(List<Command> commands)
			throws Exception {
		URL address = new URL(APP_URL + "/rest/execute");
		ClientRequest request = createRequest(address);
		// NEEDED ON 6.1
		request.header(JaxbSerializationProvider.EXECUTE_DEPLOYMENT_ID_HEADER,
				DEPLOYMENT_ID);
		JaxbCommandsRequest commandMessage = new JaxbCommandsRequest();
		commandMessage.setCommands(commands);
		commandMessage.setDeploymentId(DEPLOYMENT_ID);
		String body = convertJaxbObjectToString(commandMessage);
		System.out.println(body);
		request.body(MediaType.APPLICATION_XML, body);
		ClientResponse<String> responseObj = request.post(String.class);
		String strResponse = responseObj.getEntity();
		System.out.println("RESPONSE FROM THE SERVER: \n" + strResponse);
		JaxbCommandsResponse cmdsResp = convertStringToJaxbObject(strResponse);
		return cmdsResp.getResponses();
	}

	private ClientRequest createRequest(URL address) {
		return new ClientRequestFactory()
				.createRequest(address.toExternalForm())
				.header("Authorization", getAuthHeader())
				.header(JaxbSerializationProvider.EXECUTE_DEPLOYMENT_ID_HEADER,
						DEPLOYMENT_ID);
	}

	/**
	 * 
	 * Creates the authentication header
	 * 
	 * @return
	 */
	private String getAuthHeader() {
		String auth = USER + ":" + PASSWORD;
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset
				.forName("US-ASCII")));
		return "Basic " + new String(encodedAuth);
	}

	/**
	 * 
	 * Generate the request body. Could it be static?
	 * 
	 * @param object
	 * @return
	 * @throws JAXBException
	 */
	private String convertJaxbObjectToString(Object object)
			throws JAXBException {
		// TODO: Add here your classes
		Class<?>[] classesToBeBound = { JaxbCommandsRequest.class };
		Marshaller marshaller = JAXBContext.newInstance(classesToBeBound)
				.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		StringWriter stringWriter = new StringWriter();
		marshaller.marshal(object, stringWriter);
		String output = stringWriter.toString();
		System.out.println("REQUEST CONTENT: \n" + output);
		return output;
	}

	/**
	 * Converts the response. Could it be static?
	 * 
	 * @param str
	 * @return
	 * @throws JAXBException
	 */
	private JaxbCommandsResponse convertStringToJaxbObject(String str)
			throws JAXBException {
		Unmarshaller unmarshaller = JAXBContext.newInstance(
				JaxbCommandsResponse.class).createUnmarshaller();
		return (JaxbCommandsResponse) unmarshaller.unmarshal(new StringReader(
				str));
	}

}