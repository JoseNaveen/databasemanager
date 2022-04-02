package com.joey.databasemanager.implalpha;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.joey.databasemanager.implalpha.ClientConnection.ClientStateEvent;

public class Channel {
	
	final private static Logger log = LoggerFactory.getLogger("Channel");
	final private SocketChannel myChannel;
	
	public Channel(SocketChannel ch) {
		this.myChannel = ch;
	}
	
	
	public SelectableChannel configureBlocking(boolean b) throws IOException {
		return this.myChannel.configureBlocking(b);
	}
	
	public SelectionKey register(Selector sel,int ops) throws ClosedChannelException {
		return this.myChannel.register(sel, ops);
	}
	
	public void close() throws IOException {
		this.myChannel.close();
	}
	
	public boolean startReading(SelectionKey readkey, ClientConnection client) {
		SocketChannel channel = (SocketChannel) readkey.channel();
		int bytesread = 0;
		boolean readerror = false;
		try {
			log.info("reading data from channel to buffer");
			bytesread = channel.read(client.getBuffer());
		} catch (IOException e) {
			log.error("Error while trying to read from channel");
			readerror = true;
		}
		if (bytesread == -1 || readerror) {
			return false;
		}
		else {
			client.getBuffer().flip();
			client.handleMessage();
			return true;
		}
	}

}
