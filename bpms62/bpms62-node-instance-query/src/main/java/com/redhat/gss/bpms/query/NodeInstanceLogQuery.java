package com.redhat.gss.bpms.query;

import java.util.List;

import org.jbpm.process.audit.JPAAuditLogService;
import org.jbpm.process.audit.query.NodeInstLogQueryBuilderImpl;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.manager.audit.NodeInstanceLog;
import org.kie.internal.query.ParametrizedQuery;
import org.kie.internal.runtime.manager.audit.query.NodeInstanceLogQueryBuilder;
import org.kie.internal.runtime.manager.context.EmptyContext;

import com.redhat.gss.bpms.Util;

public class NodeInstanceLogQuery {

	private static final long PIID = 8756;
	private static final String SCRIPT_NODE = "ActionNode";

	public static void main(String[] args) {
		RuntimeManager manager = Util.getRuntimeManager();
		RuntimeEngine engine = manager.getRuntimeEngine(EmptyContext.get());
		JPAAuditLogService jpaAuditLogService = (JPAAuditLogService) engine
				.getAuditService();
		
		long startQuery = System.currentTimeMillis();
		NodeInstanceLogQueryBuilder nodeInstanceLogQueryBuilder = new NodeInstLogQueryBuilderImpl(
				jpaAuditLogService);
		nodeInstanceLogQueryBuilder.processInstanceId(PIID);
		nodeInstanceLogQueryBuilder.intersect();
		nodeInstanceLogQueryBuilder.nodeType(SCRIPT_NODE);
		ParametrizedQuery<NodeInstanceLog> nodeInstanceList = nodeInstanceLogQueryBuilder
				.build();
		List<NodeInstanceLog> nodeInstanceLogList = nodeInstanceList
				.getResultList();
		long endQuery = System.currentTimeMillis();
		long diffQuery = endQuery - startQuery;
		System.out.println("Time for Query = " + diffQuery);
		for (NodeInstanceLog node : nodeInstanceLogList) {
			System.out.printf("Node ID: %s - Node Name: %s - Date: %s \n", node.getNodeId(), node.getNodeName(), node.getDate().toString());
		}
	}

}
