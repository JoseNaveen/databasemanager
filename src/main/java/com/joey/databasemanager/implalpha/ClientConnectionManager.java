package com.joey.databasemanager.implalpha;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ClientConnectionManager {
	
	
	private static String REMOTE_SERVER_IP = System.getenv("remote");
	
	private static int REMOTE_SERVER_PORT = 5454;
	
	private Future<?> connectTaskFuture;
	
	private Future<?> readTaskFuture;
	
	
	private ExecutorService executor = Executors.newFixedThreadPool(5);
	
	private SocketChannel client;
	
	private Selector selector;
	
	public void startConnectTask() {
		Runnable connTask = () -> {
			while (true) {
				try {
					Thread.sleep(5000);
					client = SocketChannel.open(new InetSocketAddress(REMOTE_SERVER_IP, REMOTE_SERVER_PORT));
					client.configureBlocking(false);
					if (selector != null) {
						if (selector.isOpen()) {
							selector.close();
						}
					}
					selector = Selector.open();
					System.out.println("Connected to agent handler");
					startReadTask();
					return;
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		};
		
		this.connectTaskFuture = executor.submit(connTask);
	}
	
	public void startReadTask() {
		Runnable readTask =()-> {
			
		};
		
		this.readTaskFuture = executor.submit(readTask);
		
	}
	
	
}
