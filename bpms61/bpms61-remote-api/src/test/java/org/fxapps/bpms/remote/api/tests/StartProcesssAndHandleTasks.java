package org.fxapps.bpms.remote.api.tests;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.fxapps.bpms.remote.RemoteAPITestBase;
import org.junit.Test;
import org.kie.api.runtime.KieSession;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.Status;
import org.kie.api.task.model.TaskSummary;

public class StartProcesssAndHandleTasks extends RemoteAPITestBase {
	
	// check the process instance
	private static final int TOTAL_PROCESSES = 5;
	private static final String PROCESS_WITH_TASK_ASSIGNED_TO_A_GROUP = "test.proc_ht";

	@Override
	@Test
	public void doTest() throws Exception {
		List<Status> status = Arrays.asList(Status.Ready);
		KieSession ksession = engine.getKieSession();
		TaskService taskService = engine.getTaskService();
		// you could also use getTasksAssignedAsPotentialOwner(user, lang)
		List<TaskSummary> tasks = taskService.getTasksAssignedAsPotentialOwnerByStatus(USER, status, "en-UK");
		cleanTasks(taskService, tasks);
		for (int i = 0; i < TOTAL_PROCESSES; i++) {
			ksession.startProcess(PROCESS_WITH_TASK_ASSIGNED_TO_A_GROUP);
		}
		tasks = taskService.getTasksAssignedAsPotentialOwnerByStatus(USER, status, "en-UK");
		assertEquals(TOTAL_PROCESSES, tasks.size());
		cleanTasks(taskService, tasks);
	}

	private void cleanTasks(TaskService taskService, List<TaskSummary> tasks) {
		for (TaskSummary task : tasks) {
			taskService.claim(task.getId(), USER);
			taskService.start(task.getId(), USER);
			taskService.complete(task.getId(), USER, null);
		}
	}

}
