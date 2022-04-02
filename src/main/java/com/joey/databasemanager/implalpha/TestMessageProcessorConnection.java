package com.joey.databasemanager.implalpha;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestMessageProcessorConnection implements MessageProcessorConnection{

	
	private static Logger log = LoggerFactory.getLogger("TestMessageProcessorConnection");
	private Queue<TextMessage> messageQueue = new ConcurrentLinkedQueue<TextMessage>();
	private ExecutorService executor = Executors.newFixedThreadPool(1);
	
	@Override
	public Queue<TextMessage> getMessageQueue() {
		return this.messageQueue;
	}
	
	public void startMessageSending() {
		Runnable task = () -> {
			
			for (int i=0;i<5;i++) {
				synchronized(this.messageQueue) {
					var tmessage = new TextMessage();
					tmessage.setMsgPayload("hello world: " + i);
					log.info("adding message to queue");
					this.messageQueue.add(tmessage);
					this.messageQueue.notify();
				}
				
			}
		};
		
		executor.submit(task);
		
	}
	
	

}
