package com.joey.databasemanager.implalpha;

import java.util.UUID;

public class OnState extends AtomicState implements StateChild{
	final StateMachine machine;
	private CompositeState parent;
	final String id;
	
	
	public OnState(StateMachine machine) {
		this.machine = machine;
		this.id = UUID.randomUUID().toString();
	}
	
	public String getId() {
		return id;
	}
	
	public OnState(StateMachine machine,CompositeState parent) {
		this.machine = machine;
		this.parent = parent;
		this.id = UUID.randomUUID().toString();
	}


	@Override
	public void process(Message message, Controller controller) {
		if (message.getMessage().equalsIgnoreCase("off")) {
			controller.stopcommand();
		}
		if (message.getMessage().equalsIgnoreCase("disconnect")) {
			State state = machine.getState("disconnected");
			controller.stopcommand();
			machine.setState(state);
		}
		if (message.getMessage().equalsIgnoreCase("onexitoff")) {
			State state = machine.getState("off");
			machine.setState(state);
		}
	
	}
	
	@Override
	public boolean hasParent() {
		final boolean ret = this.parent==null? false:true;
		return ret;
	}

	@Override
	public CompositeState getParent() {
		return this.parent;
	}
	

}
