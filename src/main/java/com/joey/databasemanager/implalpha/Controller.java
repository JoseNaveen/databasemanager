package com.joey.databasemanager.implalpha;

import java.util.concurrent.CompletableFuture;

public abstract class Controller{
	
	public abstract CompletableFuture<Boolean> startcommand(String[] command);
	public abstract CompletableFuture<Boolean> stopcommand();
	
	public abstract void enqueue(Message message);
	
	public abstract StateMachine getStateMachine();

}
