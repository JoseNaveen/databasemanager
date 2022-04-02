package com.joey.databasemanager.implalpha;

import java.util.Queue;

public interface MessageProcessorConnection {
	public Queue<TextMessage> getMessageQueue();
}