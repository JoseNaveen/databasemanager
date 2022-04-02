package com.joey.databasemanager.implalpha;

public interface ClientState {
	
	
	public void processEvent(ClientConnection.ClientStateEvent event);
	
	public void processEnter();
	
	public void processExit();
	
	public String getStateId();

}
