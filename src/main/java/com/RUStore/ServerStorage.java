package com.RUStore;

public class ServerStorage {
	private byte[] bytes;
	
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	
	public byte[] getBytes() {
		return this.bytes;
	}
	
	public ServerStorage(byte[] bytes) {
		this.bytes = bytes;
	}
}
