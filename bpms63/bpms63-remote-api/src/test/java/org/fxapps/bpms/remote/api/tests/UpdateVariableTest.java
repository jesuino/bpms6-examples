package org.fxapps.bpms.remote.api.tests;

import org.fxapps.bpms.remote.RemoteAPITestBase;
import org.junit.Test;
import org.kie.api.runtime.KieSession;
import org.kie.remote.jaxb.gen.JaxbStringObjectPairArray;
import org.kie.remote.jaxb.gen.SetProcessInstanceVariablesCommand;
import org.kie.remote.jaxb.gen.util.JaxbStringObjectPair;

public class UpdateVariableTest extends RemoteAPITestBase {

	private static final String VAR_VALUE = "Update by the remote Java API";
	private static final String VAR_ID = "pVar";
	private static final long INSTANCE_ID = 30;

	@SuppressWarnings("unchecked")
	@Test
	public void doTest() throws Exception {
		KieSession ksession = engine.getKieSession();
		JaxbStringObjectPairArray varJaxbList = new JaxbStringObjectPairArray();
		JaxbStringObjectPair varJaxb = new JaxbStringObjectPair(VAR_ID, VAR_VALUE);
		SetProcessInstanceVariablesCommand cmd = new SetProcessInstanceVariablesCommand();
		cmd.setProcessInstanceId(INSTANCE_ID);
		varJaxbList.getItems().add(varJaxb);
		cmd.setVariables(varJaxbList);
		ksession.execute(cmd);
	}
}
