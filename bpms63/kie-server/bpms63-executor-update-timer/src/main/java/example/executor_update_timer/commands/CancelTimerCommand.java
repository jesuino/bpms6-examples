package example.executor_update_timer.commands;

import org.kie.api.executor.Command;
import org.kie.api.executor.CommandContext;
import org.kie.api.executor.ExecutionResults;

/**
 * 
 * If the given process instance Id is stopped at a timer node, this will cancel it
 * 
 * @author wsiqueir
 *
 */
public class CancelTimerCommand extends AbstractTimerActionCommand implements Command {

	

	public ExecutionResults execute(CommandContext ctx) throws Exception {
		return super.execute(ctx, updateTimerService::cancelTimer);
	}

}