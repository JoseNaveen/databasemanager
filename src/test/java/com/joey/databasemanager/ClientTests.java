package com.joey.databasemanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;


import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.internal.matchers.Any;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.joey.databasemanager.implalpha.CompositeState;
import com.joey.databasemanager.implalpha.Controller;
import com.joey.databasemanager.implalpha.Message;
import com.joey.databasemanager.implalpha.MessageImpl;
import com.joey.databasemanager.implalpha.SimpleCommandLineProcessController;
import com.joey.databasemanager.implalpha.StateMachine;



@SpringBootTest
public class ClientTests {
	
	
	/**
	 * Test flow
	 * disconnected ->connected off-> connected on in prog -> connected on
	 * -> connected off -> connected on in prog -> connected off -> disconnected
	 * -> connected off -> connected on in prog -> connected on -> connected off -> disconnected
	 * @throws InterruptedException
	 */
	
	@Test
	public void test1() throws InterruptedException {
		var sm = new StateMachine();
		var controller = mock(Controller.class);
		when(controller.getStateMachine()).thenReturn(sm);
		
		String disconnectedstateid = sm.getState("disconnected").getId();
		String connectedstateid = sm.getState("connected").getId();
		String onstateid = sm.getState("on").getId();
		String offstateid = sm.getState("off").getId();
		String oninprogressid = sm.getState("oninprogress").getId();
		
		System.out.println("disconnected state: " + disconnectedstateid);
		System.out.println("connected state: " + connectedstateid);
		System.out.println("on state: " + onstateid);
		System.out.println("on in progress state: " + oninprogressid);
		System.out.println("off state: " + offstateid);
		
		
		assertNotNull(disconnectedstateid);
		assertNotNull(offstateid);
		assertNotNull(onstateid);
		assertNotNull(connectedstateid);
		assertEquals(sm.getCurrentState().getId(),disconnectedstateid);
		
		
		sm.process(new MessageImpl("connected"), controller);
		assertEquals(sm.getCurrentState().getId(),connectedstateid);
		CompositeState cstate = (CompositeState) sm.getState("connected");
		assertEquals(cstate.getCurrentState().getId(),offstateid);
		
		
		var fut = new CompletableFuture<Boolean>();
		fut.complete(true);
		ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
		when(controller.startcommand(any(String[].class))).thenReturn(fut);
		
		
		sm.process(new MessageImpl("on","\"some\",\"test\""), controller);
		verify(controller,times(1)).enqueue(captor.capture());
		Thread.sleep(1000);
		Message message = captor.getValue();
		sm.process(message, controller);
		cstate = (CompositeState) sm.getState("connected");
		assertEquals(sm.getCurrentState().getId(),connectedstateid);
		assertEquals(cstate.getCurrentState().getId(),onstateid);
		verify(controller,times(1)).startcommand(any(String[].class));
	
		sm.process(new MessageImpl("off"), controller);
		verify(controller,times(1)).stopcommand();
		cstate = (CompositeState) sm.getState("connected");
		assertEquals(sm.getCurrentState().getId(),connectedstateid);
		assertEquals(cstate.getCurrentState().getId(),onstateid);
		
		sm.process(new MessageImpl("onexitoff"), controller);
		assertEquals(sm.getCurrentState().getId(),connectedstateid);
		cstate = (CompositeState) sm.getState("connected");
		assertEquals(cstate.getCurrentState().getId(),offstateid);
		
		
		fut = new CompletableFuture<Boolean>();
		fut.complete(false);
		when(controller.startcommand(any(String[].class))).thenReturn(fut);
		sm.process(new MessageImpl("on","\"some\",\"test\""), controller);
		verify(controller,times(2)).enqueue(captor.capture());
		Thread.sleep(1000);
		message = captor.getValue();
		sm.process(message, controller);
		cstate = (CompositeState) sm.getState("connected");
		assertEquals(sm.getCurrentState().getId(),connectedstateid);
		assertEquals(cstate.getCurrentState().getId(),offstateid);
		verify(controller,times(2)).startcommand(any(String[].class));
		
		sm.process(new MessageImpl("disconnect"), controller);
		assertEquals(sm.getCurrentState().getId(),disconnectedstateid);
		
		sm.process(new MessageImpl("on","\"some\",\"test\""), controller);
		assertEquals(sm.getCurrentState().getId(),disconnectedstateid);
		//on will be ignored
		verify(controller,times(2)).startcommand(any(String[].class));
		
		sm.process(new MessageImpl("connected"), controller);
		cstate = (CompositeState) sm.getState("connected");
		assertEquals(sm.getCurrentState().getId(),connectedstateid);
		assertEquals(cstate.getCurrentState().getId(),offstateid);
		verify(controller,times(2)).startcommand(any(String[].class));
		
		fut = new CompletableFuture<Boolean>();
		fut.complete(true);
		when(controller.startcommand(any(String[].class))).thenReturn(fut);
		sm.process(new MessageImpl("on","\"some\",\"test\""), controller);
		verify(controller,times(3)).enqueue(captor.capture());
		Thread.sleep(1000);
		message = captor.getValue();
		sm.process(message, controller);
		assertEquals(sm.getCurrentState().getId(),connectedstateid);
		cstate = (CompositeState) sm.getState("connected");
		assertEquals(cstate.getCurrentState().getId(),onstateid);
		verify(controller,times(3)).startcommand(any(String[].class));
		
		sm.process(new MessageImpl("off"), controller);
		verify(controller,times(2)).stopcommand();
		assertEquals(sm.getCurrentState().getId(),connectedstateid);
		cstate = (CompositeState) sm.getState("connected");
		assertEquals(cstate.getCurrentState().getId(),onstateid);
		
		sm.process(new MessageImpl("onexitoff"), controller);
		assertEquals(sm.getCurrentState().getId(),connectedstateid);
		cstate = (CompositeState) sm.getState("connected");
		assertEquals(cstate.getCurrentState().getId(),offstateid);
		
		
		sm.process(new MessageImpl("disconnect"), controller);
		assertEquals(sm.getCurrentState().getId(),disconnectedstateid);
	}
	
	
	/**
	 * disconnected-> connected off-> connected on in progress -> connected on -> disconnected 
	 * @throws InterruptedException
	 */
	@Test
	public void test2() throws InterruptedException {
		var sm = new StateMachine();
		var controller = mock(Controller.class);
		when(controller.getStateMachine()).thenReturn(sm);
		
		String disconnectedstateid = sm.getState("disconnected").getId();
		String connectedstateid = sm.getState("connected").getId();
		String onstateid = sm.getState("on").getId();
		String offstateid = sm.getState("off").getId();
		String oninprogressid = sm.getState("oninprogress").getId();
		
		System.out.println("disconnected state: " + disconnectedstateid);
		System.out.println("connected state: " + connectedstateid);
		System.out.println("on state: " + onstateid);
		System.out.println("on in progress state: " + oninprogressid);
		System.out.println("off state: " + offstateid);
		
		
		assertNotNull(disconnectedstateid);
		assertNotNull(offstateid);
		assertNotNull(onstateid);
		assertNotNull(connectedstateid);
		assertEquals(sm.getCurrentState().getId(),disconnectedstateid);
		
		
		sm.process(new MessageImpl("connected"), controller);
		assertEquals(sm.getCurrentState().getId(),connectedstateid);
		CompositeState cstate = (CompositeState) sm.getState("connected");
		assertEquals(cstate.getCurrentState().getId(),offstateid);
		
		
		var fut = new CompletableFuture<Boolean>();
		fut.complete(true);
		ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
		when(controller.startcommand(any(String[].class))).thenReturn(fut);
		
		
		sm.process(new MessageImpl("on","\"some\",\"test\""), controller);
		verify(controller,times(1)).enqueue(captor.capture());
		Thread.sleep(1000);
		Message message = captor.getValue();
		sm.process(message, controller);
		cstate = (CompositeState) sm.getState("connected");
		assertEquals(sm.getCurrentState().getId(),connectedstateid);
		assertEquals(cstate.getCurrentState().getId(),onstateid);
		verify(controller,times(1)).startcommand(any(String[].class));
		
		sm.process(new MessageImpl("disconnect"), controller);
		assertEquals(sm.getCurrentState().getId(),disconnectedstateid);
		verify(controller,times(1)).stopcommand();
		
	}
	
	//@Test
	public void test3() throws Exception {
		StateMachine sm = mock(StateMachine.class);
		var controller = new SimpleCommandLineProcessController(sm);
		ArgumentCaptor<Message> messagecaptor = ArgumentCaptor.forClass(Message.class);
		ArgumentCaptor<Controller> controllercaptor = ArgumentCaptor.forClass(Controller.class);
		controller.startcommand(new String[] {"C:\\Program Files\\Wireshark\\tshark.exe",
												"-i","5","-c","100","-w","test1.pcap"});
		Thread.sleep(60000);
		verify(sm,times(1)).process(messagecaptor.capture(), controllercaptor.capture());
		assertEquals(messagecaptor.getValue().getMessage(),"off");
		
	}
	
	
	@Test
	public void test4() throws Exception {
		var sm = new StateMachine();
		var controller = mock(Controller.class);
		when(controller.getStateMachine()).thenReturn(sm);
		
		String disconnectedstateid = sm.getState("disconnected").getId();
		String connectedstateid = sm.getState("connected").getId();
		String onstateid = sm.getState("on").getId();
		String offstateid = sm.getState("off").getId();
		String oninprogressid = sm.getState("oninprogress").getId();
		
		System.out.println("disconnected state: " + disconnectedstateid);
		System.out.println("connected state: " + connectedstateid);
		System.out.println("on state: " + onstateid);
		System.out.println("on in progress state: " + oninprogressid);
		System.out.println("off state: " + offstateid);
		
		
		assertNotNull(disconnectedstateid);
		assertNotNull(offstateid);
		assertNotNull(onstateid);
		assertNotNull(connectedstateid);
		assertEquals(sm.getCurrentState().getId(),disconnectedstateid);
		
		
		sm.process(new MessageImpl("connected"), controller);
		assertEquals(sm.getCurrentState().getId(),connectedstateid);
		CompositeState cstate = (CompositeState) sm.getState("connected");
		assertEquals(cstate.getCurrentState().getId(),offstateid);
		
		
		var fut = new CompletableFuture<Boolean>();
		fut.completeExceptionally(new IllegalStateException("new exception"));
		ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
		when(controller.startcommand(any(String[].class))).thenReturn(fut);
		
		
		sm.process(new MessageImpl("on","\"some\",\"test\""), controller);
		verify(controller,times(1)).enqueue(captor.capture());
		Thread.sleep(1000);
		Message message = captor.getValue();
		sm.process(message, controller);
		cstate = (CompositeState) sm.getState("connected");
		assertEquals(sm.getCurrentState().getId(),connectedstateid);
		assertEquals(cstate.getCurrentState().getId(),offstateid);
		verify(controller,times(1)).startcommand(any(String[].class));
		
		sm.process(new MessageImpl("disconnect"), controller);
		assertEquals(sm.getCurrentState().getId(),disconnectedstateid);
	}
	
	@Test
	public void test5() throws Exception {
		final StateMachine sm = new StateMachine();
		Controller controller = new SimpleCommandLineProcessController(sm);
		String disconnectedstateid = sm.getState("disconnected").getId();
		String connectedstateid = sm.getState("connected").getId();
		String onstateid = sm.getState("on").getId();
		String offstateid = sm.getState("off").getId();
		String oninprogressid = sm.getState("oninprogress").getId();
		
		System.out.println("disconnected state: " + disconnectedstateid);
		System.out.println("connected state: " + connectedstateid);
		System.out.println("on state: " + onstateid);
		System.out.println("on in progress state: " + oninprogressid);
		System.out.println("off state: " + offstateid);
		
		controller.enqueue(new MessageImpl("connected"));
		assertEquals(sm.getCurrentState().getId(),connectedstateid);
		CompositeState cstate = (CompositeState) sm.getState("connected");
		assertEquals(cstate.getCurrentState().getId(),offstateid);
		
		String command = "\"C:\\Program Files\\Wireshark\\tshark.exe\",\"-i\",\"5\",\"-c\",\"100\",\"-w\",\"test1.pcap\"";
		final MessageImpl message = new MessageImpl("on",command);
		controller.enqueue(message);
		Thread.sleep(1000);
		assertEquals(sm.getCurrentState().getId(),connectedstateid);
		cstate = (CompositeState) sm.getState("connected");
		assertEquals(cstate.getCurrentState().getId(),onstateid);
		Thread.sleep(50000);
		assertEquals(sm.getCurrentState().getId(),connectedstateid);
		cstate = (CompositeState) sm.getState("connected");
		assertEquals(cstate.getCurrentState().getId(),offstateid);
	}
	
	@Test
	public void test6() throws Exception {
		final StateMachine sm = new StateMachine();
		Controller controller = new SimpleCommandLineProcessController(sm);
		String disconnectedstateid = sm.getState("disconnected").getId();
		String connectedstateid = sm.getState("connected").getId();
		String onstateid = sm.getState("on").getId();
		String offstateid = sm.getState("off").getId();
		String oninprogressid = sm.getState("oninprogress").getId();
		
		System.out.println("disconnected state: " + disconnectedstateid);
		System.out.println("connected state: " + connectedstateid);
		System.out.println("on state: " + onstateid);
		System.out.println("on in progress state: " + oninprogressid);
		System.out.println("off state: " + offstateid);
		
		controller.enqueue(new MessageImpl("connected"));
		assertEquals(sm.getCurrentState().getId(),connectedstateid);
		CompositeState cstate = (CompositeState) sm.getState("connected");
		assertEquals(cstate.getCurrentState().getId(),offstateid);
		
		String command = "\"C:\\Program Files\\Wireshark\\tshark.exe\",\"-i\",\"5\",\"-w\",\"test2.pcap\"";
		final MessageImpl message = new MessageImpl("on",command);
		controller.enqueue(message);
		Thread.sleep(1000);
		assertEquals(sm.getCurrentState().getId(),connectedstateid);
		cstate = (CompositeState) sm.getState("connected");
		assertEquals(cstate.getCurrentState().getId(),onstateid);
		Thread.sleep(20000);
		final MessageImpl offmessage = new MessageImpl("off");
		controller.enqueue(offmessage);
		Thread.sleep(1000);
		assertEquals(sm.getCurrentState().getId(),connectedstateid);
		cstate = (CompositeState) sm.getState("connected");
		assertEquals(cstate.getCurrentState().getId(),offstateid);
	}
	
	@Test
	public void test7() throws Exception {
		final StateMachine sm = new StateMachine();
		Controller controller = new SimpleCommandLineProcessController(sm);
		String disconnectedstateid = sm.getState("disconnected").getId();
		String connectedstateid = sm.getState("connected").getId();
		String onstateid = sm.getState("on").getId();
		String offstateid = sm.getState("off").getId();
		String oninprogressid = sm.getState("oninprogress").getId();
		
		System.out.println("disconnected state: " + disconnectedstateid);
		System.out.println("connected state: " + connectedstateid);
		System.out.println("on state: " + onstateid);
		System.out.println("on in progress state: " + oninprogressid);
		System.out.println("off state: " + offstateid);
		
		controller.enqueue(new MessageImpl("connected"));
		assertEquals(sm.getCurrentState().getId(),connectedstateid);
		CompositeState cstate = (CompositeState) sm.getState("connected");
		assertEquals(cstate.getCurrentState().getId(),offstateid);
		
		String command = "\"C:\\Program Files\\Wireshark\\tshark.exe\",\"-i\",\"5\",\"-w\",\"test3.pcap\"";
		final MessageImpl message = new MessageImpl("on",command);
		controller.enqueue(message);
		Thread.sleep(1000);
		assertEquals(sm.getCurrentState().getId(),connectedstateid);
		cstate = (CompositeState) sm.getState("connected");
		assertEquals(cstate.getCurrentState().getId(),onstateid);
		Thread.sleep(20000);
		final MessageImpl disconnectmessage = new MessageImpl("disconnect");
		controller.enqueue(disconnectmessage);
		Thread.sleep(1000);
		assertEquals(sm.getCurrentState().getId(),disconnectedstateid);
	}
}
