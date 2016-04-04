package org.fxapps.bpms.remote.api.tests;

import java.util.List;

import org.fxapps.bpms.remote.RemoteAPITestBase;
import org.junit.Test;
import org.kie.api.task.model.TaskSummary;
import org.kie.remote.jaxb.gen.GetTaskAssignedAsPotentialOwnerCommand;
import org.kie.remote.jaxb.gen.QueryFilter;

public class TaskPagingTest extends RemoteAPITestBase {

	private static final String POTENTIAL_OWNER = "g1";
	
	@SuppressWarnings("unchecked")
	@Test
	public void doTest() throws Exception {
		GetTaskAssignedAsPotentialOwnerCommand cmd = new GetTaskAssignedAsPotentialOwnerCommand();
		cmd.getGroupIds().add(POTENTIAL_OWNER);
		QueryFilter filter = new QueryFilter();
		filter.setCount(5);
		filter.setOffset(0);
		cmd.setFilter(filter);
		List<TaskSummary> result =  engine.getKieSession().execute(cmd);
		for(TaskSummary t : result) {
			System.out.printf("%s %s %s", t.getName(), t.getDescription(), t.getCreatedOn());
		}

	}
}
