package com.joey.databasemanager.implalpha;

import java.util.UUID;

public class DisconnectedState extends AtomicState{
	
	final private StateMachine machine;
	final String id;
	
	public DisconnectedState(StateMachine machine) {
		this.machine = machine;
		this.id = UUID.randomUUID().toString();
		
	}
	public String getId() {
		return id;
	}
	
	
	@Override
	public void process(Message message, Controller controller) {
		if(message.getMessage().equalsIgnoreCase("connected")) {
			State state = machine.getState("connected");
			machine.setState(state);
		}
		if (message.getMessage().equalsIgnoreCase("onexitoff")) {
			System.out.println("ignoring onexitoff event");
		}
		//ignore anything else
 		
	}

	@Override
	public boolean hasParent() {
		return false;
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof AtomicState) {
			if (((AtomicState) obj).getId().equalsIgnoreCase(this.getId())) {
				return true;
			}
			else {
				return false;
			}
		}
		return false;
	}

}
