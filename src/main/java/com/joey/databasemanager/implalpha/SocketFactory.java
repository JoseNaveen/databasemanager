package com.joey.databasemanager.implalpha;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;

public class SocketFactory {
	
	
	
	public Channel create(String ip, Integer port) throws IOException {
		return new Channel(SocketChannel.open(new InetSocketAddress(ip,port)));
	}
	
	
	public SelectorImpl createSelector() throws IOException {
		return new SelectorImpl(Selector.open());
	}

}
