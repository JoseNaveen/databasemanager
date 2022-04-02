package com.joey.databasemanager.implalpha;

public abstract class AbstractClientConnection {
	
	
	
	protected abstract void closeConnection();
	public abstract void requestStopConnectionAttempt();
	public abstract boolean isStopRequested();
	public abstract void startConnectionAttempt();
	public abstract void transition(ClientState source, ClientState target);
	public abstract void requestStopMessageReaderTask();
	public abstract void startMessageReaderTask();
	
	public abstract ClientState getClientDisconnectedState();
	public abstract ClientState getClientConnectedState();
	public abstract ClientState getClientStoppedState();
}
