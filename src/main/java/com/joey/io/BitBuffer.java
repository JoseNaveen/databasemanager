package com.joey.io;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import com.joey.databasemanager.protocol.jap.DecoderException;
import com.joey.databasemanager.protocol.jap.EncodingException;

public class BitBuffer {
	
	
	/**
	 * Current used bits
	 */
	int usedBits = 0;
	/**
	 * Current size in number of bits
	 */
	int size = 8;
	
	/**
	 * Byte array for storing the
	 * contents of this Bit Container instance
	 */
	byte[] value;
	
	/**
	 * Bits that have been read already
	 */
	int readBits = 0;
	
	public enum Mode{
		MODE1,
		MODE2;
	}
	
	final private Mode mode;
	final private Strategy strategy;
	final static Map<Integer, Integer> mask;
	
	static {
		mask = new HashMap<>(8);
		mask.put(1, 0x01);
		mask.put(2, 0x03);
		mask.put(3, 0x07);
		mask.put(4, 0x0F);
		mask.put(5, 0x1F);
		mask.put(6, 0x3F);
		mask.put(7, 0x7F);
		mask.put(8, 0xFF);
	}
	
	public BitBuffer(BitBuffer.Mode mode) {
		this(new byte[] {(byte)0,(byte)0},mode);
		
	}
	
	public BitBuffer(int size, BitBuffer.Mode mode) {
		this.value = new byte[size];
		this.mode = mode;
		if (mode.equals(Mode.MODE1)) {
			this.strategy = new LSBStrategy(this);
		}
		else 
		{
			this.strategy = new MSBStrategy(this);
		}
		this.size = size * 8;
	}
	
	public BitBuffer(byte[] val,BitBuffer.Mode mode) {
		this.mode = mode;
		if (mode.equals(Mode.MODE1)) {
			this.strategy = new LSBStrategy(this);
		}
		else 
		{
			this.strategy = new MSBStrategy(this);
		}
		
		this.value = val;
		size = val.length * 8;
	}
	void increase(int increaseby) {
		byte[] newarray = new byte[(size/8) + 1*increaseby];
		for(int i = 0;i<value.length;i++) {
			/**
			 * Copy the old content to new byte array index
			 */
			newarray[i] = value[i];
		}
		for(int i = size/8;i<(size/8)+1;i++) {
			/**
			 * Initialize value to zero
			 */
			newarray[i] = (byte)0;
		}
		value = newarray;
		size = size + 8*increaseby;
	}
	
	/**
	 * @param val value to be set
	 * @param numberofbits number of bits to be used for setting the value
	 * @throws EncodingException
	 */
	public void addInteger(int val, int numberofbits) throws EncodingException{
		
		this.strategy.addInteger(val, numberofbits);
		
		
	}
	
	public int getInteger(int numberofbits) throws DecoderException {
		return this.strategy.getInteger(numberofbits);
	}
	
	public byte[] array() {
		if (usedBits % 8 == 0) {
			if (usedBits == 0) {
				throw new RuntimeException();
			}
			int final_size = usedBits/8;
			byte[] final_array = new byte[final_size];
			for (int i = 0;i<final_size;i++) {
				final_array[i] = this.value[i];
			}
			return final_array;
		}
		else {
			int final_size = usedBits/8 + 1;
			byte[] final_array = new byte[final_size];
			for (int i = 0;i<final_size;i++) {
				final_array[i] = this.value[i];
			}
			return final_array;
		}
		
	}
	
	public boolean isFull() {
		if (size-usedBits==0) {
			return true;
		}
		return false;
	}
	
	public byte[] peek(byte[] b) {
		return null;
	}
	
	public int peek(int numberofbits) {
		return 0;
	}
	
	
	
}