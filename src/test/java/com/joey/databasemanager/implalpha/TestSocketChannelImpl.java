package com.joey.databasemanager.implalpha;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestSocketChannelImpl extends SocketChannel{

	
	private static Logger log = LoggerFactory.getLogger("TestSocketChannelImpl");
	
	protected TestSocketChannelImpl() {
		super(SelectorProvider.provider());
	}

	
	@Override
	public <T> T getOption(SocketOption<T> name) throws IOException {
		
		return null;
	}

	@Override
	public Set<SocketOption<?>> supportedOptions() {
		
		return null;
	}

	@Override
	public SocketChannel bind(SocketAddress local) throws IOException {
		
		return null;
	}

	@Override
	public <T> SocketChannel setOption(SocketOption<T> name, T value) throws IOException {
		
		return null;
	}

	@Override
	public SocketChannel shutdownInput() throws IOException {
		
		return null;
	}

	@Override
	public SocketChannel shutdownOutput() throws IOException {
		
		return null;
	}

	@Override
	public Socket socket() {
		
		return null;
	}

	@Override
	public boolean isConnected() {
		
		return false;
	}

	@Override
	public boolean isConnectionPending() {
		
		return false;
	}

	@Override
	public boolean connect(SocketAddress remote) throws IOException {
		
		return false;
	}

	@Override
	public boolean finishConnect() throws IOException {
		
		return false;
	}

	@Override
	public SocketAddress getRemoteAddress() throws IOException {
		
		return null;
	}

	@Override
	public int read(ByteBuffer dst) throws IOException {
		
		return 0;
	}

	@Override
	public long read(ByteBuffer[] dsts, int offset, int length) throws IOException {
		
		return 0;
	}

	@Override
	public int write(ByteBuffer src) throws IOException {
		
		return 0;
	}

	@Override
	public long write(ByteBuffer[] srcs, int offset, int length) throws IOException {
		
		return 0;
	}

	@Override
	public SocketAddress getLocalAddress() throws IOException {
		
		return null;
	}

	@Override
	protected void implCloseSelectableChannel() throws IOException {
		

	}

	@Override
	protected void implConfigureBlocking(boolean block) throws IOException {
		

	}
}
