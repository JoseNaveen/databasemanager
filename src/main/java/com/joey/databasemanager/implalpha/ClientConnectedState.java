package com.joey.databasemanager.implalpha;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.joey.databasemanager.implalpha.ClientConnection.ClientStateEvent;

public class ClientConnectedState implements ClientState {

	private AbstractClientConnection connection;
	private String stateid;
	private static Logger log = LoggerFactory.getLogger("ClientConnectedState");

	@Override
	public void processEvent(ClientStateEvent event) {
		if (event.equals(ClientStateEvent.STOPPED)) {
			log.info("processing event: " + event.toString());
			var stoppedstate = connection.getClientStoppedState();
			connection.transition(this, stoppedstate);
		}
	}
	
	public ClientConnectedState(AbstractClientConnection conn) {
		this.connection = conn;
		this.stateid = UUID.randomUUID().toString();
	}
	
	public String getStateId() {
		return this.stateid;
	}

	@Override
	public void processEnter() {
		this.connection.startMessageReaderTask();
	}

	@Override
	public void processExit() {
		this.connection.requestStopMessageReaderTask();
		
	}

}
