package com.joey.databasemanager.implalpha;

public class TestClientConnection extends AbstractClientConnection {
	
	
	private ClientState clientDisconnectedState;
	private ClientState clientConnectedState;
	private ClientState clientStoppedState;
	private ClientState currentState;
	
	public TestClientConnection() {
		clientDisconnectedState = new ClientDisconnectedState(this);
		clientConnectedState = new ClientConnectedState(this);
		clientStoppedState = new ClientStoppedState(this);
		currentState = clientStoppedState;
	}
	@Override
	protected void closeConnection() {
		

	}

	@Override
	public void requestStopConnectionAttempt() {
		

	}

	@Override
	public boolean isStopRequested() {
		
		return false;
	}

	@Override
	public void startConnectionAttempt() {
		
	}

	@Override
	public void transition(ClientState source, ClientState target) {
		source.processExit();
		this.currentState = target;
		this.currentState.processEnter();
	}

	@Override
	public void startMessageReaderTask() {
		

	}

	@Override
	public ClientState getClientDisconnectedState() {
		
		return this.clientDisconnectedState;
	}

	@Override
	public ClientState getClientConnectedState() {
		
		return this.clientConnectedState;
	}

	@Override
	public ClientState getClientStoppedState() {
		
		return this.clientStoppedState;
	}
	@Override
	public void requestStopMessageReaderTask() {
		
	}

}
