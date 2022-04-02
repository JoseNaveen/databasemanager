package com.joey.databasemanager.implalpha;

import java.io.File;

public class FileTransferClient {
	
	final private FileTransferConnection fconn;
	
	public FileTransferClient(FileTransferConnection conn) {
		this.fconn = conn;
	}
	
	
	
	public void sendFile(String filePath) {
		File f = new File(filePath);
		long size = f.length();
		
	}
	
	private void sendFilename(String filename) {
		
	}
	
	private void sendFileSize(long fileSize) {
		
	}
	
	private void sendFileParts(String filePath) {
		
	}

}
