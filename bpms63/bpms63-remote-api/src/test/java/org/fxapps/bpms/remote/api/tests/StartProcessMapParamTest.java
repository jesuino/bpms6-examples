package org.fxapps.bpms.remote.api.tests;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.fxapps.bpms.remote.RemoteAPITestBase;
import org.junit.Test;
import org.kie.api.runtime.KieSession;

public class StartProcessMapParamTest extends RemoteAPITestBase {

	private static final String PROCESS_ID = "project1.proc_map_param";

	@Test
	public void doTest() throws Exception {
		KieSession kSession = engine.getKieSession();
		Map<String, Object> params = new HashMap<>();
		Items items = new Items();
		items.setMyItems(new HashMap<String, Object>());
		params.put("p1", items);
		long piid = kSession.startProcess(PROCESS_ID, params).getId();
		System.out.println(piid);
	}

	@XmlRootElement
	public static class Items {
		private Map<String, Object> myItems;

		public Map<String, Object> getMyItems() {
			return myItems;
		}

		public void setMyItems(Map<String, Object> myItems) {
			this.myItems = myItems;
		}

	}
}
