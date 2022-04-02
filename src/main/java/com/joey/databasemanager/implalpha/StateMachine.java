package com.joey.databasemanager.implalpha;

public class StateMachine {

	private State disconnectedState;
	private State connectedState;
	private State initial  = disconnectedState;
	private State currentstate = disconnectedState;
	
	
	public StateMachine() {
		disconnectedState = new DisconnectedState(this);
		connectedState = new ConnectedState(this);
		initial  = disconnectedState;
		currentstate = disconnectedState;
	}

	public synchronized void process(Message message, Controller controller) {
		this.currentstate.process(message, controller);
	}
	
	public State getState(String str) {
		if (str.equalsIgnoreCase("disconnected")) {
			return this.disconnectedState;
		}
		if (str.equalsIgnoreCase("connected")) {
			return this.connectedState;
		}
		if (str.equalsIgnoreCase("on") || str.equalsIgnoreCase("off") || str.equalsIgnoreCase("oninprogress")) {
			CompositeState cstate = (CompositeState) this.connectedState;
			return cstate.getState(str);
		}
		
		throw new IllegalStateException("Unknown State: " + str);
	}
	
	public void setState(State state) {
		if (state.hasParent()) {
			System.out.println("State id: " + state.getId());
			System.out.println("state has parent");
			StateChild c = (StateChild) state;
			CompositeState parent = c.getParent();
			if (parent.equals(this.currentstate)) {
				parent.setState(state);
				return;
			}
			else {
				this.currentstate = parent;
				parent.setState(state);
				return;
			}
		}
		System.out.println("current state is: " + this.currentstate.getId());
		System.out.println("setting state to: " + state.getId());
		this.currentstate = state;
		if (state.isComposite()) {
			CompositeState cstate = (CompositeState) state;
			cstate.initialize();
		}
		
	}
	
	public State getCurrentState() {
		return this.currentstate;
	}
	
	
}
