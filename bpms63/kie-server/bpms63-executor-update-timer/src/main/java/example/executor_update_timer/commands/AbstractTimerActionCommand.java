package example.executor_update_timer.commands;

import java.util.function.BiConsumer;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.transaction.UserTransaction;

import org.kie.api.executor.CommandContext;
import org.kie.api.executor.ExecutionResults;

import example.executor_update_timer.service.UpdateTimerService;

public abstract class AbstractTimerActionCommand {

	protected UpdateTimerService updateTimerService;

	Logger logger = Logger.getLogger(this.getClass().getName());

	public AbstractTimerActionCommand() {
		updateTimerService = UpdateTimerService.Factory.get();
	}

	public ExecutionResults execute(CommandContext ctx,
			BiConsumer<String, Long> action) throws Exception {
		UserTransaction ut = (UserTransaction) InitialContext
				.doLookup("java:jboss/UserTransaction");
		try {
			ut.begin();
			String identifier = (String) ctx.getData().get("identifier");
			// avoid errors if users send processInstanceId as text
			String piidStr = String.valueOf(ctx.getData().get(
					"processInstanceId"));
			long piid = Long.parseLong(piidStr);
			logger.warning("Running action " + action + " on identifier "
					+ identifier + " and process instance " + piid);
			action.accept(identifier, piid);
			ut.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ut.rollback();
		}
		return null;
	}

}