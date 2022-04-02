package com.joey.databasemanager.implalpha;

import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageProcessor {
	
	private static Logger log = LoggerFactory.getLogger("MessageProcessor");
	private boolean requestShutdown = false;
	
	
	final private MessageProcessorConnection conn;
	private ExecutorService executor = Executors.newFixedThreadPool(1);
	
	public MessageProcessor(MessageProcessorConnection mconn) {
		this.conn = mconn;
	}
	
	public synchronized void requestShutdown() {
		synchronized (conn.getMessageQueue()) {
			conn.getMessageQueue().notify();
		}
		this.requestShutdown = true;
	}
	
	public synchronized boolean isShutDownRequested() {
		return this.requestShutdown;
	}
	
	
	public void startMessageReader() {
		this.requestShutdown = false;
		Runnable task = ()-> {
			while(!isShutDownRequested()) {
				Queue<TextMessage> queue = this.conn.getMessageQueue();
				
				synchronized(queue) {
					while (!queue.isEmpty()) {
						TextMessage tmessage = queue.remove();
						log.info(tmessage.getMsgPayload());
					}
					try {
						log.info("waiting...");
						queue.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
			}
			log.info("shutting down");
			
		};
		
		this.executor.submit(task);
	}
	
	

}
