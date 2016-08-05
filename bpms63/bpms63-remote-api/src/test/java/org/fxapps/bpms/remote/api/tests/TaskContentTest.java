package org.fxapps.bpms.remote.api.tests;

import java.util.Map;

import org.fxapps.bpms.remote.RemoteAPITestBase;
import org.junit.Test;
import org.kie.api.task.TaskService;

public class TaskContentTest extends RemoteAPITestBase {

	private static final long taskId = 18;

	@Test
	public void doTest() throws Exception {
		TaskService service = engine.getTaskService();
		Map<String, Object> taskContent = service.getTaskContent(taskId);
		System.out.println(taskContent.get("in_param"));
	}
}