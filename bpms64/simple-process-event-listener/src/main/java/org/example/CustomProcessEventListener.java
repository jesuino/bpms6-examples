package org.example;

import org.kie.api.event.process.ProcessCompletedEvent;
import org.kie.api.event.process.ProcessEventListener;
import org.kie.api.event.process.ProcessNodeLeftEvent;
import org.kie.api.event.process.ProcessNodeTriggeredEvent;
import org.kie.api.event.process.ProcessStartedEvent;
import org.kie.api.event.process.ProcessVariableChangedEvent;

public class CustomProcessEventListener implements ProcessEventListener {

	public void beforeProcessStarted(ProcessStartedEvent event) {
	}

	public void afterProcessStarted(ProcessStartedEvent event) {
	}

	public void beforeProcessCompleted(ProcessCompletedEvent event) {
	}

	public void afterProcessCompleted(ProcessCompletedEvent event) {
	}

	public void beforeNodeTriggered(ProcessNodeTriggeredEvent event) {
		System.out.println("Before Node triggered: " + event.getNodeInstance().getNodeName());

	}

	public void afterNodeTriggered(ProcessNodeTriggeredEvent event) {
	}

	public void beforeNodeLeft(ProcessNodeLeftEvent event) {
	}

	public void afterNodeLeft(ProcessNodeLeftEvent event) {
	}

	public void beforeVariableChanged(ProcessVariableChangedEvent event) {
	}

	public void afterVariableChanged(ProcessVariableChangedEvent event) {
	}

}