package com.joey.databasemanager.implalpha;

import java.util.UUID;

public class OnInProgressState extends AtomicState implements StateChild {

	
	private final String id = UUID.randomUUID().toString();
	final private CompositeState parent;
	final private StateMachine machine;
	
	
	
	public OnInProgressState(StateMachine machine,CompositeState parent) {
		this.parent = parent;
		this.machine = machine;
	}
	
	
	@Override
	public CompositeState getParent() {
		return parent;
	}

	@Override
	public void process(Message message, Controller controller) {
		if (message.getMessage().equalsIgnoreCase("onsuccess")) {
			System.out.println("on in progress processing on success event");
			State state = machine.getState("on");
			machine.setState(state);
			return;
		}
		if (message.getMessage().equalsIgnoreCase("off")) {
			State state = machine.getState("off");
			machine.setState(state);
			return;
		}
	}

	@Override
	public boolean hasParent() {
		return true;
	}

	@Override
	public String getId() {
		return id;
	}

}
