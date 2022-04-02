package com.joey.databasemanager.implalpha;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.joey.databasemanager.implalpha.ClientConnection.ClientStateEvent;

public class ClientDisconnectedState implements ClientState {
	private static Logger log = LoggerFactory.getLogger("ClientDisconnectedState");
	private AbstractClientConnection connection;
	private ExecutorService executor = Executors.newSingleThreadExecutor();
	private Future<?> future;
	private String stateid;

	@Override
	public void processEvent(ClientStateEvent event) {
		if (event.equals(ClientStateEvent.CONNECTED)) {
			ClientState targetstate = connection.getClientConnectedState();
			connection.transition(this, targetstate);
		}
		if (event.equals(ClientStateEvent.STOPPED)) {
			ClientState targetstate = connection.getClientStoppedState();
			connection.transition(this, targetstate);
		}
	}
	
	public ClientDisconnectedState(AbstractClientConnection conn) {
		this.connection = conn;
		this.stateid = UUID.randomUUID().toString();
	}
	
	public String getStateId() {
		return this.stateid;
	}

	
	@Override
	public void processEnter() {
		this.connection.startConnectionAttempt();
	}

	@Override
	public void processExit() {
		log.info("processing exit: " + "clientdisconnectedstate");
		this.connection.requestStopConnectionAttempt();
	}

}
