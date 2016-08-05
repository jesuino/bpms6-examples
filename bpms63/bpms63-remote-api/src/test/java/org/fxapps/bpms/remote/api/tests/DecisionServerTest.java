package org.fxapps.bpms.remote.api.tests;

import java.util.ArrayList;
import java.util.List;

import org.drools.core.command.impl.GenericCommand;
import org.drools.core.command.runtime.BatchExecutionCommandImpl;
import org.drools.core.command.runtime.rule.GetObjectsCommand;
import org.junit.Test;
import org.kie.api.command.BatchExecutionCommand;
import org.kie.api.runtime.ClassObjectFilter;
import org.kie.internal.runtime.helper.BatchExecutionHelper;

public class DecisionServerTest {
	
	@Test
	public void testModifyFact() {
		GetObjectsCommand getObjectCommand = new GetObjectsCommand(new ClassObjectFilter(this.getClass()));
		getObjectCommand.setOutIdentifier("test");
		List<GenericCommand<?>> commands = new ArrayList<GenericCommand<?>>();
        commands.add(getObjectCommand);
        BatchExecutionCommand command = new BatchExecutionCommandImpl(commands);
        System.out.println(BatchExecutionHelper.newXStreamMarshaller().toXML(command));		
	}

}