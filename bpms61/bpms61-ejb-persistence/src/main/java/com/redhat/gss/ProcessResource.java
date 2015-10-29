package com.redhat.gss;

import java.util.Calendar;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/process")
@Stateless
public class ProcessResource {

	@Inject
	ProcessBean bean;

	@GET
	@Path("start")
	public String start() {
		bean.startProcess();
		return "Process start at " + Calendar.getInstance().getTime();
	}
}
