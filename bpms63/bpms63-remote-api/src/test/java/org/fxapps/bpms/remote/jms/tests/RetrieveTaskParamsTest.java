package org.fxapps.bpms.remote.jms.tests;

import java.nio.charset.Charset;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.codec.binary.Base64;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientRequestFactory;
import org.junit.Test;
import org.kie.remote.jaxb.gen.Task;
// TODO: Fix this class, it is not working 
public class RetrieveTaskParamsTest {

	final String BASE_URL = "http://localhost:8080/business-central/rest";
	private static final String USER = "jesuino";
	private static final String PASSWORD = "redhat2014!";
	final int TASK_ID = 5;
	final String URL_TASK = BASE_URL + "/task/" + TASK_ID;
	final String URL_TASK_CONTENT = BASE_URL + "/task/content/";
	final String TASK_VAR = "tV";

	@Test
	public void doTest() throws Exception {
		// Let's retrieve the task to find its content map id
		Task t = createRequest(URL_TASK).get(Task.class).getEntity();
		long contentId = t.getTaskData().getDocumentContentId();
		// Then we retrieve the content map
		Content c = createRequest(URL_TASK_CONTENT + contentId).get(
				Content.class).getEntity();
		for (Entry e : c.getContentMap().getEntries()) {
			System.out.println(e.getKey());
		}
	}

	static private ClientRequest createRequest(String url) {
		return new ClientRequestFactory().createRequest(url).header(
				"Authorization", getAuthHeader());
	}

	static private String getAuthHeader() {
		String auth = USER + ":" + PASSWORD;
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset
				.forName("US-ASCII")));
		return "Basic " + new String(encodedAuth);
	}

	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Content {
		private long id;
		private byte[] content;
		private ContentMap contentMap;
		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public byte[] getContent() {
			return content;
		}

		public void setContent(byte[] content) {
			this.content = content;
		}

		public ContentMap getContentMap() {
			return contentMap;
		}

		public void setContentMap(ContentMap contentMap) {
			this.contentMap = contentMap;
		}
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name="contentMap")
	public static class ContentMap {
		@XmlElementWrapper(name = "contentMap")
		private List<Entry> entries;

		public List<Entry> getEntries() {
			return entries;
		}

		public void setEntries(List<Entry> entries) {
			this.entries = entries;
		}
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Entry {
		@XmlAttribute(name = "key")
		private String key;
		@XmlAttribute(name = "class-name")
		private String className;
		@XmlElement(name = "entry")
		private byte[] content;

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}

		public byte[] getContent() {
			return content;
		}

		public void setContent(byte[] content) {
			this.content = content;
		}
	}
}