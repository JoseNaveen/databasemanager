package com.joey.databasemanager.implalpha;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;

public class SelectorImpl {

	
	final private Selector selector;
	
	public SelectorImpl(Selector s) {
		this.selector = s;
	}
	
	
	public int select() throws IOException {
		return this.selector.select();
	}
	
	public void close() throws IOException {
		selector.close();
	}
	
	public boolean isOpen() {
		return this.selector.isOpen();
	}
	
	public Set<SelectionKey> selectedKeys() {
		return this.selector.selectedKeys();
	}
	
	public Selector getSelector() {
		return this.selector;
	}
	
	
}
