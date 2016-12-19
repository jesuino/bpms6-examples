package example.executor_update_timer.service;

import org.drools.core.command.impl.CommandBasedStatefulKnowledgeSession;
import org.drools.core.command.impl.KnowledgeCommandContext;
import org.drools.core.impl.StatefulKnowledgeSessionImpl;
import org.jbpm.process.instance.InternalProcessRuntime;
import org.jbpm.process.instance.command.UpdateTimerCommand;
import org.jbpm.process.instance.timer.TimerManager;
import org.jbpm.workflow.instance.WorkflowProcessInstance;
import org.jbpm.workflow.instance.node.TimerNodeInstance;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.process.NodeInstance;
import org.kie.internal.runtime.manager.RuntimeManagerRegistry;
import org.kie.internal.runtime.manager.context.ProcessInstanceIdContext;

class UpdateTimerServiceImpl implements UpdateTimerService {

	private RuntimeManager runtimeManager;

	public void setAsTriggered(String identifier, long piid) {
		RuntimeEngine runtimeEngine = getRuntimeEngine(identifier, piid);
		KieSession kSession = runtimeEngine.getKieSession();
		WorkflowProcessInstance pi = (WorkflowProcessInstance) kSession
				.getProcessInstance(piid);
		TimerNodeInstance oldTimerInstance = getTimerInstance(pi);
		oldTimerInstance.triggerCompleted(true);
		dispose(runtimeEngine);
	}

	public void cancelTimer(String identifier, long piid) {
		RuntimeEngine runtimeEngine = getRuntimeEngine(identifier, piid);
		KieSession kSession = runtimeEngine.getKieSession();
		WorkflowProcessInstance pi = (WorkflowProcessInstance) kSession
				.getProcessInstance(piid);
		TimerNodeInstance timerInstance = getTimerInstance(pi);
		TimerManager tm = getTimerManager(kSession);
		tm.cancelTimer(timerInstance.getId());
		dispose(runtimeEngine);
	}

	public void updateTimerNode(Long piid, String identifier, long delay,
			long period, int repeatLimit) {
		RuntimeEngine runtimeEngine = getRuntimeEngine(identifier, piid);
		KieSession kSession = runtimeEngine.getKieSession();
		WorkflowProcessInstance pi = (WorkflowProcessInstance) kSession
				.getProcessInstance(piid);
		TimerNodeInstance timerInstance = getTimerInstance(pi);
		UpdateTimerCommand cmd = new UpdateTimerCommand(piid, timerInstance
				.getTimerNode().getName(), delay, period, repeatLimit);
		kSession.execute(cmd);
		dispose(runtimeEngine);
	}


	private static TimerManager getTimerManager(KieSession ksession) {
		KieSession internal = ksession;
		if (ksession instanceof CommandBasedStatefulKnowledgeSession) {
			internal = ((KnowledgeCommandContext) ((CommandBasedStatefulKnowledgeSession) ksession)
					.getCommandService().getContext()).getKieSession();
		}
		return ((InternalProcessRuntime) ((StatefulKnowledgeSessionImpl) internal)
				.getProcessRuntime()).getTimerManager();
	}

	private TimerNodeInstance getTimerInstance(WorkflowProcessInstance pi) {
		TimerNodeInstance oldTimerInstance = null;
		for (NodeInstance n : pi.getNodeInstances()) {
			if (n instanceof TimerNodeInstance)
				oldTimerInstance = (TimerNodeInstance) n;
		}
		if (oldTimerInstance == null) {
			throw new IllegalArgumentException(
					"Process is NOT stopped on a timer instance");
		}
		return oldTimerInstance;
	}

	private void dispose(RuntimeEngine runtimeEngine) {
		runtimeManager.disposeRuntimeEngine(runtimeEngine);
	}
	
	private RuntimeEngine getRuntimeEngine(String identifier, Long piid) {
		runtimeManager = getRuntimeManager(identifier);
		RuntimeEngine runtimeEngine = null;
		runtimeEngine = runtimeManager
				.getRuntimeEngine(ProcessInstanceIdContext.get(piid));
		return runtimeEngine;
	}

	private RuntimeManager getRuntimeManager(String identifier) {
		RuntimeManager runtimeManager = RuntimeManagerRegistry.get()
				.getManager(identifier);

		if (runtimeManager == null) {
			throw new IllegalStateException(
					"There is no runtime manager for identifier " + identifier);
		}

		return runtimeManager;
	}
}
