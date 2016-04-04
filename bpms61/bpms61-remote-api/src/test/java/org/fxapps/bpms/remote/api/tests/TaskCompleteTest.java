package org.fxapps.bpms.remote.api.tests;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fxapps.bpms.remote.RemoteAPITestBase;
import org.junit.Test;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.TaskSummary;

public class TaskCompleteTest extends RemoteAPITestBase {

	private static final String PROCESS_ID = "proj1.proc_with_ht";
	private static final String USR = "jesuino";

	@Test
	public void doTest() throws Exception {

		TaskService service = engine.getTaskService();
		ProcessInstance pi = engine.getKieSession().startProcess(PROCESS_ID);
		List<TaskSummary> tasks = service.getTasksAssignedAsPotentialOwner(USR, "en-UK");
		System.out.println("Process created: " + pi.getId());
		System.out.println("Number of tasks: " + tasks.size());
		long taskId = 0l;
		for (TaskSummary taskSummary : tasks) {
			if (taskSummary.getProcessInstanceId() == pi.getId()) {
				taskId = taskSummary.getId();
			}
		}
		if (taskId != 0l) {
			System.out.println("Found task: " + taskId);
			service.claim(taskId, USR);
			service.start(taskId, USR);
			service.complete(taskId, USR, getParameters());
		}
	}

	private Map<String, Object> getParameters() {
		Map<String, Object> params = new HashMap<>();
		params.put("_taskVar", ">> PASSED BY REMOTE API <<");
		return params;
	}
}