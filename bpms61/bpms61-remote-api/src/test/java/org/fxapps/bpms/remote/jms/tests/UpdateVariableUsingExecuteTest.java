package org.fxapps.bpms.remote.jms.tests;

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
import org.fxapps.bpms.remote.RemoteAPITestBase;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientRequestFactory;
import org.jboss.resteasy.client.ClientResponse;
import org.junit.Test;
import org.kie.api.command.Command;
import org.kie.remote.client.jaxb.JaxbCommandsRequest;
import org.kie.remote.client.jaxb.JaxbCommandsResponse;
import org.kie.remote.jaxb.gen.JaxbStringObjectPairArray;
import org.kie.remote.jaxb.gen.SetProcessInstanceVariablesCommand;
import org.kie.remote.jaxb.gen.util.JaxbStringObjectPair;
import org.kie.services.client.serialization.JaxbSerializationProvider;
import org.kie.services.client.serialization.jaxb.impl.JaxbCommandResponse;

public class UpdateVariableUsingExecuteTest extends RemoteAPITestBase {

	private static final String VAR_VALUE = "Update by the remote HTTP API using /execute";
	private static final String VAR_ID = "pVar";
	private static final long INSTANCE_ID = 30;

	@Test
	public void doTest() throws Exception {
        List<Command> commands = new ArrayList<>();	 
        SetProcessInstanceVariablesCommand updateVarCommand = new SetProcessInstanceVariablesCommand();
        JaxbStringObjectPairArray params = new JaxbStringObjectPairArray();
        params.getItems().add(new JaxbStringObjectPair(VAR_ID, VAR_VALUE));
        updateVarCommand.setProcessInstanceId(INSTANCE_ID);
        updateVarCommand.setVariables(params);
        commands.add(updateVarCommand);
        List<JaxbCommandResponse<?>> response = executeCommand(DEPLOYMENT_ID,
                commands);
        System.out.printf("Command %s executed.\n", response.toString());
        System.out.println("commands1" + commands);	 
    }
 
    static List<JaxbCommandResponse<?>> executeCommand(String deploymentId,
            List<Command> commands) throws Exception {
        URL address = new URL(APP_URL + "/rest/execute");
        ClientRequest request = createRequest(address);
        // NEEDED ON 6.1
        request.header(JaxbSerializationProvider.EXECUTE_DEPLOYMENT_ID_HEADER, DEPLOYMENT_ID);
        JaxbCommandsRequest commandMessage = new JaxbCommandsRequest();
        commandMessage.setCommands(commands);
        commandMessage.setDeploymentId(DEPLOYMENT_ID);
        String body = convertJaxbObjectToString(commandMessage);
        request.body(MediaType.APPLICATION_XML, body); 
        ClientResponse<String> responseObj = request.post(String.class);
        String strResponse = responseObj.getEntity();
        System.out.println("RESPONSE FROM THE SERVER: \n" + strResponse);
        JaxbCommandsResponse cmdsResp = convertStringToJaxbObject(strResponse);
        return cmdsResp.getResponses();
    }
 
    static private ClientRequest createRequest(URL address) {
        return new ClientRequestFactory().createRequest(
                address.toExternalForm()).header("Authorization",
                getAuthHeader());
    }
 
    static private String getAuthHeader() {
        String auth = USER + ":" + PASSWORD;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
        return "Basic " + new String(encodedAuth);
    }
 
    static String convertJaxbObjectToString(Object object) throws JAXBException {
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
 
    static JaxbCommandsResponse convertStringToJaxbObject(String str)
            throws JAXBException {
        Unmarshaller unmarshaller = JAXBContext.newInstance(
                JaxbCommandsResponse.class).createUnmarshaller();
        return (JaxbCommandsResponse) unmarshaller.unmarshal(new StringReader(
                str));
    }
}
