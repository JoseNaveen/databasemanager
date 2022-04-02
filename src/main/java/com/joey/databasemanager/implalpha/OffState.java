package com.joey.databasemanager.implalpha;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class OffState extends AtomicState implements StateChild {
	
	final private StateMachine machine;
	private CompositeState parent;
	final String id;
	
	
	public OffState(StateMachine machine) {
		this.machine = machine;
		this.id = UUID.randomUUID().toString();
	}
	public String getId() {
		return id;
	}


	public OffState(StateMachine machine, CompositeState parent) {
		this.machine = machine;
		this.parent = parent;
		this.id = UUID.randomUUID().toString();
	}
	
	private String[] parseCommands(String command) {
		String[] commands = command.split(",");
		return commands;
	}

	@Override
	public void process(Message message, Controller controller) {
		
		if (message.getMessage().equalsIgnoreCase("onsuccess")) {
			System.out.println("off state processing on success event");
		}
		if (message.getMessage().equalsIgnoreCase("on")) {
			System.out.println("off state processing on event");
			State state = machine.getState("oninprogress");
			

			final String[] parseCommands = parseCommands(message.getCommand());
			final CompletableFuture<Boolean> startcommand = controller.startcommand(parseCommands);
			machine.setState(state);
			startcommand.exceptionally(ex-> {
				ex.printStackTrace();
				return false;
				}).
				thenAccept((b) -> {
				System.out.println("executing callback");
				if (b) {
					System.out.println("onsuccess callback");
					controller.enqueue(new MessageImpl("onsuccess"));
				} else {
					System.out.println("on failure callback triggering off");
					controller.enqueue(new MessageImpl("off"));
				}
			});

			
		}
		if (message.getMessage().equalsIgnoreCase("off")) {
			//ignore;
		}
		if (message.getMessage().equalsIgnoreCase("disconnect")) {
			System.out.println("off state processing disconnect event");
			State state = machine.getState("disconnected");
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
