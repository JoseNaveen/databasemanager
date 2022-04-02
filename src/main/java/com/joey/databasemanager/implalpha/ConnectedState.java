package com.joey.databasemanager.implalpha;

import java.util.UUID;

public class ConnectedState extends CompositeState{

	private State onState;
	private State offState;
	private State onInProgressState;
	private State initial = offState;
	private State currentState = offState;
	private StateMachine machine;
	final String id;
	
	
	public ConnectedState(StateMachine machine) {
		this.id = UUID.randomUUID().toString();
		onState = new OnState(machine,this);
		offState = new OffState(machine,this);
		onInProgressState = new OnInProgressState(machine, this);
		currentState = offState;
		initial = offState;
		this.machine = machine;
	}
	
	

	@Override
	public void process(Message message, Controller controller) {
		
		this.currentState.process(message, controller);
	}
	
	public State getState(String str) {
		if (str.equalsIgnoreCase("on")) {
			return this.onState;
		}
		else if (str.equalsIgnoreCase("off")){
			return this.offState;
		}
		else if (str.equalsIgnoreCase("oninprogress")) {
			return this.onInProgressState;
		}
		throw new IllegalStateException("Unknown State: " + str);
	}
	
	@Override
	public boolean hasParent() {
		return false;
	}



	@Override
	public void setState(State state) {
		this.currentState = state;
		
	}
	
	public String getId() {
		return id;
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof CompositeState) {
			if (((CompositeState) obj).getId().equalsIgnoreCase(this.getId())) {
				return true;
			}
			else {
				return false;
			}
		}
		return false;
	}



	@Override
	public State getCurrentState() {
		return this.currentState;
	}
	
	public void initialize() {
		this.currentState = this.initial;
	}
}
