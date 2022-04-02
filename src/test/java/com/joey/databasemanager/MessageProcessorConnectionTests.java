package com.joey.databasemanager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.joey.databasemanager.implalpha.ClientConnection;
import com.joey.databasemanager.implalpha.MessageProcessor;
import com.joey.databasemanager.implalpha.MessageProcessorConnection;
import com.joey.databasemanager.implalpha.TestMessageProcessorConnection;

@SpringBootTest
public class MessageProcessorConnectionTests {

	
	//@Test
	public void test1() throws Exception{
		TestMessageProcessorConnection mconn = new TestMessageProcessorConnection();
		
		var mproc = new MessageProcessor(mconn);
		
		
		mproc.startMessageReader();
		mconn.startMessageSending();
		Thread.sleep(5000);
		mproc.requestShutdown();
		Thread.sleep(2000);
		
		
	}
	
	@Test
	public void test2() throws Exception {
		var clientConn = new ClientConnection();
		
		clientConn.start();
		Thread.sleep(19000);
		clientConn.stop();
		Thread.sleep(5000);
		clientConn.start();
		Thread.sleep(19000);
		clientConn.stop();
		Thread.sleep(2000);
		
		
	}
}
