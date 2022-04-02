package com.joey.databasemanager.implalpha;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.Iterator;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ClientConnection extends AbstractClientConnection implements FileTransferConnection{
	
	private static Logger log = LoggerFactory.getLogger("ClientConnection");
	
	private ClientState clientDisconnectedState;
	private ClientState clientConnectedState;
	private ClientState clientStoppedState;
	private ClientState currentState;
	private Channel client;
	private SelectorImpl selector;
	private static String REMOTE_SERVER_IP = "127.0.0.1";
	private static int REMOTE_SERVER_PORT = 5454;
	private ExecutorService executor = Executors.newFixedThreadPool(5);
	private Future<?> connectTaskFuture;
	private ByteBuffer buffer;
	private Queue<TextMessage> messageQueue = new ConcurrentLinkedQueue<TextMessage>();
	private Queue<TextMessage> sendQueue = new ConcurrentLinkedQueue<TextMessage>();
	
	private Queue<FilePacket> fileTransferSendQueue = new ConcurrentLinkedQueue<FilePacket>();
	
	public Queue<FilePacket> getFileTransferSendQueue() {
		return this.fileTransferSendQueue;
	}

	public Queue<TextMessage> getSendQueue() {
		return sendQueue;
	}
	private boolean stoprequest = false;
	
	private boolean stopmessagereader = false;

	private Future<?> connectionAttemptTask;

	private Future<?> messagereadertask;

	private SocketFactory factory;
	
	
	public enum ClientStateEvent {
		STARTED,
		CONNECTED,
		STOPPED,
		DISCONNECTED,
	}
	
	public ClientConnection() {
		clientDisconnectedState = new ClientDisconnectedState(this);
		clientConnectedState = new ClientConnectedState(this);
		clientStoppedState = new ClientStoppedState(this);
		log.info("Stopped stateid: " + this.clientStoppedState.getStateId());
		log.info("Connected stateid: " + this.clientConnectedState.getStateId());
		log.info("Disconnected stateid: " + this.clientDisconnectedState.getStateId());
		currentState = clientStoppedState;
		buffer = ByteBuffer.allocate(1027);
	}
	
	public void setFactory(SocketFactory f) {
		this.factory = f;
	}
	
	public void start() {
		this.currentState.processEvent(ClientStateEvent.STARTED);
	}
	
	public void stop() {
		this.currentState.processEvent(ClientStateEvent.STOPPED);
	}
	
	protected void closeConnection() {
		try {
			if (client!=null) {
					log.info("closing channel");
				client.close();
			}
		}
		catch (IOException e) {
			log.error("Exception while closing channel");
			e.printStackTrace();
		}
		try {
			if (selector!=null) {
				log.info("closing selector");
				selector.close();
			}
		}
		catch (IOException e) {
			log.error("exception while closing selector");
			e.printStackTrace();
		}
	}
	
	public synchronized void requestStopConnectionAttempt() {
		log.info("requesting stop attempt");
		if (this.connectionAttemptTask!=null && !this.connectionAttemptTask.isDone()) {
			this.connectionAttemptTask.cancel(true);
		}
		this.stoprequest = true;
	}
	
	public synchronized boolean isStopRequested() {
		return this.stoprequest;
	}
	
	public void startConnectionAttempt() {
		this.stoprequest = false;
		Runnable connTask = () -> {
			log.info("Starting connection attempt task");
			while (!isStopRequested()) {
				try {
					log.info("Attempting connection after 5 secs...");
					TimeUnit.SECONDS.sleep(5);
					client = factory.create(REMOTE_SERVER_IP, REMOTE_SERVER_PORT);
					client.configureBlocking(false);
					if (selector != null) {
						if (selector.isOpen()) {
							selector.close();
						}
					}
					selector = factory.createSelector();
					log.info("Connected to agent handler");
					this.currentState.processEvent(ClientStateEvent.CONNECTED);
					return;
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
					this.closeConnection();
				}
			}
			log.info("Shutting down connection attempt task");
		};
		this.connectionAttemptTask = this.executor.submit(connTask);

	}
	public void transition(ClientState source, ClientState target) {
		log.info("Transitioning from: "+ source.getStateId() + " to: " + target.getStateId());
		source.processExit();
		this.currentState = target;
		target.processEnter();
		
	}
	
	public synchronized void requestStopMessageReaderTask() {
		log.info("request stopping message reader task");
		this.stopmessagereader = true;
	}
	
	public synchronized boolean isStopMessageReaderRequested() {
		return this.stopmessagereader;
	}
	
	public void startMessageReaderTask() {
		this.stopmessagereader = false;
		Runnable task = () -> {
			try {
				log.info("Starting message reader task");
				client.register(selector.getSelector(), SelectionKey.OP_READ);
				while (!this.isStopMessageReaderRequested()) {
					log.info("Selector blocking");
					selector.select();
					log.info("Selector unblocking");
					Set<SelectionKey> keys = selector.selectedKeys();
					Iterator<SelectionKey> iter = keys.iterator();
					while (iter.hasNext()) {
						SelectionKey readkey = iter.next();
						iter.remove();
						if (readkey.isReadable()) {
							this.startReading(readkey);
						}
					}
				}
			} catch (IOException e) {
				log.error("IOException: select error");
				log.error("shutting down message reader task");
				e.printStackTrace();
			}
			log.info("message reader task shutting down");
		};
		
		messagereadertask = executor.submit(task);
	}
	
	public void startReading(SelectionKey readkey) {
		
		boolean b = this.client.startReading(readkey, this);
		
		if (!b) {
			try {
				readkey.cancel();
				selector.close();
				this.currentState.processEvent(ClientStateEvent.DISCONNECTED);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}
	
	public void handleMessage() {
		
		buffer.mark();
		byte mtype = buffer.get();
		if (((mtype^(byte)1)&0xFF)==0) {
			//type is control message
			var tmessage = new TextMessage();
			tmessage.setMsgType(1);
			//get length
			
			if (buffer.limit() - buffer.position() <= 2) {
				/**
				 * Only length or partial length is available partial read;
				 */
				buffer.reset();
				buffer.compact();
				return;
			}
			if (buffer.limit() - buffer.position() > 2) {
				/**
				 * Some are all data available
				 */
				byte[] len = new byte[2];
				buffer.get(len);
				int lengthInteger = ((( len[0]&0xFF)<<8) | ((len[1])&0xFF)) & 0xFFFF;
				if (buffer.limit() - buffer.position() < lengthInteger) {
					/**
					 * Only partial payload is available
					 */
					buffer.reset();
					buffer.compact();
					return;
				}
				/**
				 * Whole date is available
				 */
				tmessage.setMsgLength(lengthInteger);
				byte[] payload = new byte[lengthInteger];
				tmessage.setMsgPayload(new String(payload));
				/**
				 * Put the message in message queue
				 */
				this.messageQueue.add(tmessage);
				/**
				 * Check if more data is available to be read
				 */
				if (buffer.hasRemaining()) {
					this.handleMessage();
				}
			}
		}
	}
	
	public void startHeartBeatTask() {
		
	}
	
	public ClientState getClientDisconnectedState() {
		return clientDisconnectedState;
	}
	public ClientState getClientConnectedState() {
		return clientConnectedState;
	}
	public ClientState getClientStoppedState() {
		return clientStoppedState;
	}
	
	public ClientState getCurrentState() {
		return this.currentState;
	}
	
	public ByteBuffer getBuffer() {
		return this.buffer;
	}
	
	

}
