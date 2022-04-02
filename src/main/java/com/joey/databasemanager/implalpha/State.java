package com.joey.databasemanager.implalpha;

public abstract class State {
	
	
	public abstract void process(Message message,Controller controller);
	public abstract boolean isComposite();
	public abstract boolean hasParent();
	public abstract String getId();

}
