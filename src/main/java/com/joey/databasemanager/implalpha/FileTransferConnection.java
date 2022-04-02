package com.joey.databasemanager.implalpha;

import java.util.Queue;

public interface FileTransferConnection {
	
	
	public Queue<FilePacket> getFileTransferSendQueue();

}
