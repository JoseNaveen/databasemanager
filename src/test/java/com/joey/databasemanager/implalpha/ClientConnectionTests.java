package com.joey.databasemanager.implalpha;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.matchers.Any;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import com.joey.databasemanager.implalpha.ClientConnection.ClientStateEvent;

import javassist.Modifier;
import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class ClientConnectionTests {
	
	
	private static Logger log = LoggerFactory.getLogger("ClientConnectionTests");
	/**
	 * Test stopped -> disconnected
	 * @throws Exception
	 */
	@Test
	public void test1() throws Exception {
		AbstractClientConnection clientconn = mock(AbstractClientConnection.class);
		var clientstoppedstate = new ClientStoppedState(clientconn);
		var clientdisconnectedstate = new ClientDisconnectedState(clientconn);
		var clientconnectedstate = new ClientConnectedState(clientconn);
		
		when(clientconn.getClientConnectedState()).thenReturn(clientconnectedstate);
		when(clientconn.getClientDisconnectedState()).thenReturn(clientdisconnectedstate);
		when(clientconn.getClientStoppedState()).thenReturn(clientstoppedstate);
		
		clientstoppedstate.processEvent(ClientStateEvent.STARTED);
		ArgumentCaptor<ClientState> sourcecaptor = ArgumentCaptor.forClass(ClientState.class);
		ArgumentCaptor<ClientState> targetcaptor = ArgumentCaptor.forClass(ClientState.class);
		
		verify(clientconn,times(1)).transition(sourcecaptor.capture(), targetcaptor.capture());
		assertEquals(sourcecaptor.getValue(),clientstoppedstate);
		assertEquals(targetcaptor.getValue(),clientdisconnectedstate);
		targetcaptor.getValue().processEnter();
		verify(clientconn,times(1)).startConnectionAttempt();
		
	}
	
	/**
	 * stopped -> disconnected -> connected
	 * @throws Exception
	 */
	@Test
	public void test2() throws Exception {
		AbstractClientConnection clientconn = mock(AbstractClientConnection.class);
		var clientstoppedstate = new ClientStoppedState(clientconn);
		var clientdisconnectedstate = new ClientDisconnectedState(clientconn);
		var clientconnectedstate = new ClientConnectedState(clientconn);
		
		when(clientconn.getClientConnectedState()).thenReturn(clientconnectedstate);
		when(clientconn.getClientDisconnectedState()).thenReturn(clientdisconnectedstate);
		when(clientconn.getClientStoppedState()).thenReturn(clientstoppedstate);
		
		clientstoppedstate.processEvent(ClientStateEvent.STARTED);
		clientdisconnectedstate.processEvent(ClientStateEvent.CONNECTED);
		ArgumentCaptor<ClientState> sourcecaptor = ArgumentCaptor.forClass(ClientState.class);
		ArgumentCaptor<ClientState> targetcaptor = ArgumentCaptor.forClass(ClientState.class);
		
		verify(clientconn,times(2)).transition(sourcecaptor.capture(), targetcaptor.capture());
		assertEquals(sourcecaptor.getAllValues().get(1),clientdisconnectedstate);
		assertEquals(targetcaptor.getAllValues().get(1),clientconnectedstate);
		targetcaptor.getValue().processEnter();
		verify(clientconn,times(1)).startMessageReaderTask();
	}
	
	/**
	 * stopped -> disconnected -> connected -> stopped
	 * @throws Exception
	 */
	@Test
	public void test3() throws Exception {
		AbstractClientConnection clientconn = mock(AbstractClientConnection.class);
		var clientstoppedstate = new ClientStoppedState(clientconn);
		var clientdisconnectedstate = new ClientDisconnectedState(clientconn);
		var clientconnectedstate = new ClientConnectedState(clientconn);
		
		when(clientconn.getClientConnectedState()).thenReturn(clientconnectedstate);
		when(clientconn.getClientDisconnectedState()).thenReturn(clientdisconnectedstate);
		when(clientconn.getClientStoppedState()).thenReturn(clientstoppedstate);
		
		clientstoppedstate.processEvent(ClientStateEvent.STARTED);
		clientdisconnectedstate.processEvent(ClientStateEvent.CONNECTED);
		clientconnectedstate.processEvent(ClientStateEvent.STOPPED);
		ArgumentCaptor<ClientState> sourcecaptor = ArgumentCaptor.forClass(ClientState.class);
		ArgumentCaptor<ClientState> targetcaptor = ArgumentCaptor.forClass(ClientState.class);
		
		verify(clientconn,times(3)).transition(sourcecaptor.capture(), targetcaptor.capture());
		assertEquals(sourcecaptor.getAllValues().get(2),clientconnectedstate);
		assertEquals(targetcaptor.getAllValues().get(2),clientstoppedstate);
		sourcecaptor.getValue().processExit();
		verify(clientconn,times(1)).requestStopMessageReaderTask();
		
	}
	
	
	/**
	 * stopped -> disconnected -> stopped
	 * @throws Exception
	 */
	@Test
	public void test4() throws Exception {
		AbstractClientConnection clientconn = mock(AbstractClientConnection.class);
		var clientstoppedstate = new ClientStoppedState(clientconn);
		var clientdisconnectedstate = new ClientDisconnectedState(clientconn);
		var clientconnectedstate = new ClientConnectedState(clientconn);
		
		when(clientconn.getClientConnectedState()).thenReturn(clientconnectedstate);
		when(clientconn.getClientDisconnectedState()).thenReturn(clientdisconnectedstate);
		when(clientconn.getClientStoppedState()).thenReturn(clientstoppedstate);
		
		clientstoppedstate.processEvent(ClientStateEvent.STARTED);
		clientdisconnectedstate.processEvent(ClientStateEvent.STOPPED);
		ArgumentCaptor<ClientState> sourcecaptor = ArgumentCaptor.forClass(ClientState.class);
		ArgumentCaptor<ClientState> targetcaptor = ArgumentCaptor.forClass(ClientState.class);
		
		verify(clientconn,times(2)).transition(sourcecaptor.capture(), targetcaptor.capture());
		assertEquals(sourcecaptor.getValue(),clientdisconnectedstate);
		assertEquals(targetcaptor.getValue(),clientstoppedstate);
		sourcecaptor.getValue().processExit();
		verify(clientconn,times(1)).requestStopConnectionAttempt();
		
	}
	
	@Test
	public void test5() throws Exception {
		ClientConnection client = new ClientConnection();
		SocketFactory factory = mock(SocketFactory.class);
		
		client.setFactory(factory);
		Channel channel = mock(Channel.class);
		when(factory.create(any(String.class), any(Integer.class))).thenReturn(channel);
		SelectorImpl selector = mock(SelectorImpl.class);
		when(factory.createSelector()).thenReturn(selector);
		assertEquals(client.getCurrentState(),client.getClientStoppedState());
		when(channel.configureBlocking(any(boolean.class))).thenReturn(null);
		when(selector.select()).thenReturn(5);
		when(channel.register(any(Selector.class), any(int.class))).thenReturn(null);
		client.start();
		TimeUnit.SECONDS.sleep(7);
		verify(factory,times(1)).create(any(String.class), any(Integer.class));
		
		assertEquals(client.getCurrentState(),client.getClientConnectedState());
		
		TimeUnit.MILLISECONDS.sleep(100);
		client.stop();
		TimeUnit.SECONDS.sleep(1);
		assertEquals(client.getCurrentState(),client.getClientStoppedState());
		TimeUnit.MILLISECONDS.sleep(150);
	}
	
	/**
	 * Tests a connection refused exception
	 * @throws Exception
	 */
	@Test
	public void test6() throws Exception {
		ClientConnection client = new ClientConnection();
		SocketFactory factory = mock(SocketFactory.class);
		
		client.setFactory(factory);
		when(factory.create(any(String.class), any(Integer.class))).thenThrow(new IOException("Dummy Connection refused"));
		client.start();
		TimeUnit.MILLISECONDS.sleep(11000);
		client.stop();
		TimeUnit.MILLISECONDS.sleep(100);
		
	}
	
	/**
	 * Tests a selector creator exception
	 * @throws Exception
	 */
	@Test
	public void test7() throws Exception {
		ClientConnection client = new ClientConnection();
		SocketFactory factory = mock(SocketFactory.class);
		
		client.setFactory(factory);
		Channel channel = mock(Channel.class);
		when(factory.create(any(String.class), any(Integer.class))).thenReturn(channel);
		when(factory.createSelector()).thenThrow(new IOException("Dummy selector creator exception"));
		client.start();
		TimeUnit.MILLISECONDS.sleep(11000);
		client.stop();
		TimeUnit.MILLISECONDS.sleep(100);
	}
	
	/**
	 * Tests successful connection after a failure and then stops the connection
	 * @throws Exception
	 */
	@Test
	public void test8() throws Exception {
		ClientConnection client = new ClientConnection();
		SocketFactory factory = mock(SocketFactory.class);
		
		client.setFactory(factory);
		Channel channel = mock(Channel.class);
		when(factory.create(any(String.class), any(Integer.class))).thenThrow(new IOException("Dummy connection refused Exception"));
		SelectorImpl selector = mock(SelectorImpl.class);
		when(factory.createSelector()).thenReturn(selector);
		client.start();
		TimeUnit.MILLISECONDS.sleep(5100);
		when(factory.create(any(String.class), any(Integer.class))).thenReturn(channel);
		TimeUnit.MILLISECONDS.sleep(5200);
		client.stop();
		TimeUnit.MILLISECONDS.sleep(100);
	}
	
	
}
