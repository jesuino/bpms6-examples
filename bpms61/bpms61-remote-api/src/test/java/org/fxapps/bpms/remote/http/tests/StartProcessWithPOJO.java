package org.fxapps.bpms.remote.http.tests;

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
import org.kie.api.command.Command;
import org.kie.remote.client.jaxb.JaxbCommandsRequest;
import org.kie.remote.client.jaxb.JaxbCommandsResponse;
import org.kie.remote.jaxb.gen.JaxbStringObjectPairArray;
import org.kie.remote.jaxb.gen.StartProcessCommand;
import org.kie.remote.jaxb.gen.util.JaxbStringObjectPair;
import org.kie.services.client.serialization.JaxbSerializationProvider;
import org.kie.services.client.serialization.jaxb.impl.JaxbCommandResponse;


/**
 * 
 * A really simple client for the /execute endpoint for BPM Suite 6.1
 * 
 * 
 * @author wsiqueir
 *
 */
public class StartProcessWithPOJO {

	  /*
	   * Set the parameters according your installation
	   */
	  private static final String DEPLOYMENT_ID = "com.mycompany:remote-process-start-with-bean:1.0";
	  private static final String PROCESS_ID = "remote-process-start-with-bean.process_that_uses_mypojo";
	  private static final String PROCESS_PARAM_NAME = "myPOJO";
	  private static final String APP_URL = "http://localhost:8080/business-central/rest";
	  private static final String USER = "jesuino";
	  private static final String PASSWORD = "redhat2014!";



	    public static void main(String[] args) throws Exception {
	        // Our list of commands to be executed;
	        List<Command> commands = new ArrayList<>();	 
	        // a sample command to start a process
	        StartProcessCommand startProcessCommand = new StartProcessCommand();
	        JaxbStringObjectPairArray params = new JaxbStringObjectPairArray();
	        // Add your process parameters here
	        //params.getItems().add(new JaxbStringObjectPair(PROCESS_PARAM_NAME, new MyPOJO("My POJO TESTING")));
	        startProcessCommand.setProcessId(PROCESS_ID);
	        startProcessCommand.setParameter(params);
	        commands.add(startProcessCommand);
	        List<JaxbCommandResponse<?>> response = executeCommand(DEPLOYMENT_ID,
	                commands);
	        System.out.printf("Command %s executed.\n", response.toString());
	        System.out.println("commands1" + commands);	 
	    }
	 
	    static List<JaxbCommandResponse<?>> executeCommand(String deploymentId,
	            List<Command> commands) throws Exception {
	        URL address = new URL(APP_URL + "/execute");
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
	        Class<?>[] classesToBeBound = { JaxbCommandsRequest.class, /*MyPOJO.class*/ };
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
