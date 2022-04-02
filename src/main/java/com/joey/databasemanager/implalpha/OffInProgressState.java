package com.joey.databasemanager.implalpha;

import java.util.UUID;

public class OffInProgressState extends AtomicState implements StateChild {
	
	private final String id = UUID.randomUUID().toString();
	final private CompositeState parent;
	final private StateMachine machine;
	
	
	
	public OffInProgressState(StateMachine machine,CompositeState parent) {
		this.parent = parent;
		this.machine = machine;
	}

	@Override
	public CompositeState getParent() {
		return parent;
	}

	@Override
	public void process(Message message, Controller controller) {

	}

	@Override
	public boolean hasParent() {
		return true;
	}

	@Override
	public String getId() {
		return this.id;
	}

}
