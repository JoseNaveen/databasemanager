package com.joey.databasemanager.implalpha;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.joey.databasemanager.implalpha.ClientConnection.ClientStateEvent;

public class ClientStoppedState implements ClientState {

	
	private static Logger log = LoggerFactory.getLogger("ClientStoppedState");
	private AbstractClientConnection connection;
	private String stateid;

	@Override
	public void processEvent(ClientStateEvent event) {
		if (event.equals(ClientStateEvent.STARTED)) {
			ClientState targetstate = connection.getClientDisconnectedState();
			connection.transition(this, targetstate);		
		}
	}
	
	public ClientStoppedState(AbstractClientConnection conn) {
		this.connection = conn;
		this.stateid = UUID.randomUUID().toString();
	}
	
	public String getStateId() {
		return this.stateid;
	}

	@Override
	public void processEnter() {
		connection.closeConnection();
	}

	@Override
	public void processExit() {
		
	}

}
