package example.executor_update_timer.commands;

import org.kie.api.executor.Command;
import org.kie.api.executor.CommandContext;
import org.kie.api.executor.ExecutionResults;

import example.executor_update_timer.service.UpdateTimerService;

/**
 * 
 * If a process instance is stopped on a timer we can set it as triggered and
 * make the process skip it.
 * 
 * @author wsiqueir
 *
 */
public class SetTimerAsTriggeredCommand implements Command {

	public ExecutionResults execute(CommandContext ctx) throws Exception {
		UpdateTimerService updateTimerService = UpdateTimerService.Factory
				.get();
		String identifier = (String) ctx.getData().get("identifier");
		// avoid errors if users send processInstanceId as text
		String piidStr = String.valueOf(ctx.getData().get("processInstanceId"));
		long piid = Long.parseLong(piidStr);
		updateTimerService.setAsTriggered(identifier, piid);
		return null;
	}

}