package com.joey.databasemanager.implalpha;

public abstract class CompositeState extends State{
	
	
	public abstract State getState(String str);
	public boolean isComposite() {
		return true;
	}
	public abstract void setState(State state);
	public abstract State getCurrentState();
	public abstract void initialize();
}
