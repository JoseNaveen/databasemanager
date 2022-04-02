package com.joey.databasemanager.implalpha;

public class FilePacket {
	
	
	enum FilePacketType {
		FILENAME(10),
		FILESIZE(11),
		STARTOFFILE(12),
		ENDOFFILEWODATA(13),
		ENDOFFILEWDATA(14);
		
		private int typeValue;
		
		
		private FilePacketType(int i) {
			this.typeValue = i;
		}
		
		public int getTypeValue() {
			return this.typeValue;
		}
	}
	private byte packetType;
	private byte[] length;
	private byte[] payload;
	
	
	
	public byte getPacketType() {
		return packetType;
	}



	public void setPacketType(byte packetType) {
		this.packetType = packetType;
	}



	public byte[] getLength() {
		return length;
	}



	public void setLength(byte[] length) {
		this.length = length;
	}



	public byte[] getPayload() {
		return payload;
	}



	public void setPayload(byte[] payload) {
		this.payload = payload;
	}



	public static class Builder {
		
		private FilePacketType packetType;
		private long packetLength;
		private byte[] packetPayload;
		
		public Builder setPacketType(FilePacketType ptype) {
			this.packetType = ptype;
			return this;
		}
		
		public Builder setPacketLength(long plength) {
			this.packetLength = plength;
			return this;
		}
		
		public Builder setPacketPayload(byte[] pload) {
			this.packetPayload = pload;
			return this;
		}
		
		public FilePacket build() {
			var filePacket = new FilePacket();
			filePacket.setPacketType((byte)this.packetType.getTypeValue());
			
			return filePacket;
		}
	}
}
