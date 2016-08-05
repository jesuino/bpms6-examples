package org.fxapps.bpms.remote.plainjms.tests;

import java.net.URL;
import java.util.Properties;
import java.util.UUID;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.Test;
import org.kie.remote.client.jaxb.ClientJaxbSerializationProvider;
import org.kie.remote.client.jaxb.JaxbCommandsRequest;
import org.kie.remote.jaxb.gen.StartProcessCommand;
import org.kie.services.client.serialization.JaxbSerializationProvider;
import org.kie.services.client.serialization.SerializationConstants;

/**
 * 
 * Start a process using JMS.
 * 
 * @author wsiqueir
 * 
 */
public class StartProcessPlainJMSTest {

	private static final String DEPLOYMENT_ID = "org.kie.example:project1:1.0";
	private static final String PROCESS_ID = "project1.hello";
	private static final String APP_URL = "http://127.0.0.1:8080/business-central";
	private static final String USER = "jesuino";
	private static final String PASSWORD = "redhat2014!";

	@Test
	public void sendJms() throws Exception {
		Queue sendQueue;
		StartProcessCommand cmd = new StartProcessCommand();
		cmd.setProcessId(PROCESS_ID);
		JaxbCommandsRequest req = new JaxbCommandsRequest(DEPLOYMENT_ID, cmd);
		InitialContext context = getRemoteJbossInitialContext(new URL(APP_URL),
				USER, PASSWORD);
		ConnectionFactory connectionFactory;
		connectionFactory = (ConnectionFactory) context
				.lookup("jms/RemoteConnectionFactory");
		sendQueue = (Queue) context.lookup("jms/queue/KIE.SESSION");
		sendJmsCommands(DEPLOYMENT_ID, USER, req, connectionFactory, sendQueue,
				USER, PASSWORD, 5);
	}

	private void sendJmsCommands(String deploymentId, String user,
			JaxbCommandsRequest req, ConnectionFactory factory,
			Queue sendQueue, String jmsUser, String jmsPassword, int timeout)
			throws Exception {
		req.setUser(user);
		Connection connection = null;
		Session session = null;
		JaxbSerializationProvider serializationProvider;
		String corrId = UUID.randomUUID().toString();
		MessageProducer producer;
		connection = factory.createConnection(jmsUser, jmsPassword);
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		producer = session.createProducer(sendQueue);
		connection.start();
		serializationProvider = ClientJaxbSerializationProvider.newInstance();
		TextMessage msg = session.createTextMessage();
		msg.setJMSCorrelationID(corrId);
		msg.setStringProperty(
				SerializationConstants.DEPLOYMENT_ID_PROPERTY_NAME,
				deploymentId);
		msg.setIntProperty(
				SerializationConstants.SERIALIZATION_TYPE_PROPERTY_NAME,
				JaxbSerializationProvider.JMS_SERIALIZATION_TYPE);
		msg.setText(serializationProvider.serialize(req));
		System.out.println("Request to be send: " + serializationProvider.serialize(req));
		producer.send(msg);
		connection.close();
	}

	private InitialContext getRemoteJbossInitialContext(URL url, String user,
			String password) throws NamingException {
		Properties initialProps = new Properties();
		initialProps.setProperty(InitialContext.INITIAL_CONTEXT_FACTORY,
				"org.jboss.naming.remote.client.InitialContextFactory");
		String jbossServerHostName = url.getHost();
		initialProps.setProperty(InitialContext.PROVIDER_URL, "remote://"
				+ jbossServerHostName + ":4447");
		initialProps.setProperty(InitialContext.SECURITY_PRINCIPAL, user);
		initialProps.setProperty(InitialContext.SECURITY_CREDENTIALS, password);
		return new InitialContext(initialProps);
	}
}