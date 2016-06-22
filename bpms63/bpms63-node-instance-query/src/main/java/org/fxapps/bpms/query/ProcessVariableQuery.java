package org.fxapps.bpms.query;

import java.util.List;

import org.fxapps.bpms.Util;
import org.jbpm.process.audit.JPAAuditLogService;
import org.jbpm.process.audit.query.VarInstLogQueryBuilderImpl;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.manager.audit.VariableInstanceLog;
import org.kie.internal.query.ParametrizedQuery;
import org.kie.internal.runtime.manager.audit.query.VariableInstanceLogQueryBuilder;
import org.kie.internal.runtime.manager.context.EmptyContext;

public class ProcessVariableQuery {

	private static final String VAR_ID = "myVar";
	private static final String VAR_VALUE = "someValue";

	public static void main(String[] args) {
		RuntimeManager manager = Util.getRuntimeManager();
		RuntimeEngine engine = manager.getRuntimeEngine(EmptyContext.get());
		JPAAuditLogService jpaAuditLogService = (JPAAuditLogService) engine
				.getAuditService();
		VariableInstanceLogQueryBuilder varQueryBuilder = new VarInstLogQueryBuilderImpl(jpaAuditLogService);
		ParametrizedQuery<VariableInstanceLog> queryVariable = varQueryBuilder.variableId(VAR_ID).value(VAR_VALUE).build();
		List<VariableInstanceLog> resultList = queryVariable.getResultList();
		System.out.printf("Number of process that has variable %s with value %s: %d\n", VAR_ID, VAR_VALUE, resultList.size());
		for (VariableInstanceLog variableInstanceLog : resultList) {
			System.out.printf("Process ID: %s - Instance ID: %d ", variableInstanceLog.getProcessId(), variableInstanceLog.getProcessInstanceId());
		}
	}

}
