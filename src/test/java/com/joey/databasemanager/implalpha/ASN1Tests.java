package com.joey.databasemanager.implalpha;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.BitSet;

import org.junit.jupiter.api.Test;
import org.springframework.core.codec.DecodingException;

import com.joey.databasemanager.protocol.jap.ConversionUtils;
import com.joey.io.BitBuffer;
import com.joey.io.BitBuffer.Mode;

public class ASN1Tests {
	
	
	
	//@Test
	public void test2() throws Exception {
		BitSet bs = BitSet.valueOf(new byte[] {(byte)5});
		
		System.out.println(ConversionUtils.toString(bs.toByteArray()));
	}
	
	//@Test
	public void test1() throws Exception {
		
		var container = new BitBuffer(Mode.MODE1);
		
		container.addInteger(5, 6);
		container.addInteger(4, 4);
		container.addInteger(7, 6);
		container.addInteger(15, 8);
		container.addInteger(15, 4);
		container.addInteger(15, 4);
		System.out.println(ConversionUtils.toString(container.array()));
		assertTrue(ConversionUtils.toString(container.array()).equalsIgnoreCase("051d0fff"));
		
		
	}
	//@Test
	public void test3() throws Exception {
		
		var container = new BitBuffer(Mode.MODE1);
		
		container.addInteger(0, 4);
		container.addInteger(7, 4);
		container.addInteger(7, 6);
		container.addInteger(3, 2);
		System.out.println(ConversionUtils.toString(container.array()));
		assertTrue(ConversionUtils.toString(container.array()).equalsIgnoreCase("70c7"));
		
		
	}
	
	//@Test
	public void test4() throws Exception {
		
		var container = new BitBuffer(Mode.MODE1);
		
		container.addInteger(15, 6);
		container.addInteger(30, 16);
		container.addInteger(3, 2);
		
		System.out.println(ConversionUtils.toString(container.array()));
		assertTrue(ConversionUtils.toString(container.array()).equalsIgnoreCase("8f07c0"));
		
		
	}
	
	//@Test
	public void test5() throws Exception {
		
		var container = new BitBuffer(Mode.MODE1);
		
		container.addInteger(1, 1);
		container.addInteger(1, 1);
		container.addInteger(1, 1);
		container.addInteger(1, 1);
		container.addInteger(1, 1);
		container.addInteger(1, 1);
		container.addInteger(1, 1);
		container.addInteger(1, 1);
		
		System.out.println(ConversionUtils.toString(container.array()));
		assertTrue(ConversionUtils.toString(container.array()).equalsIgnoreCase("ff"));
	}
	
	//@Test
	public void test6() throws Exception {
		
		var container = new BitBuffer(Mode.MODE1);
		container.addInteger(15, 6);
		container.addInteger(30, 12);
		container.addInteger(3, 6);
		System.out.println(ConversionUtils.toString(container.array()));
		assertTrue(ConversionUtils.toString(container.array()).equalsIgnoreCase("8f070c"));
	}
	
	//@Test
	public void test7() throws Exception {
		var container = new BitBuffer(ConversionUtils.decodeHexString("8f070c"),Mode.MODE1);
		int i = container.getInteger(6);
		assertEquals(i,15);
		i = container.getInteger(12);
		assertEquals(i,30);
		i = container.getInteger(6);
		assertEquals(i,3);
	}
	
	//@Test
	public void test8() throws Exception {
		var container = new BitBuffer(ConversionUtils.decodeHexString("8f07c0"),Mode.MODE1);
		
		int i = container.getInteger(6);
		assertEquals(i,15);
		i = container.getInteger(16);
		assertEquals(i,30);
		i = container.getInteger(2);
		assertEquals(i,3);
	}
	
	@Test
	public void test9() throws Exception {
		var container = new BitBuffer(Mode.MODE2);
		container.addInteger(3, 2);
		container.addInteger(2, 2);
		container.addInteger(2, 2);
		container.addInteger(15, 12);
		container.addInteger(3, 2);
		container.addInteger(3, 4);
		assertEquals(ConversionUtils.toString(container.array()),"e803f3");
	}
	
	@Test
	public void test10() throws Exception {
		var container = new BitBuffer(Mode.MODE2);
		container.addInteger(1, 1);
		container.addInteger(1, 1);
		container.addInteger(0, 1);
		container.addInteger(0, 1);
		container.addInteger(1, 1);
		container.addInteger(1, 1);
		container.addInteger(2, 2);
		container.addInteger(8, 4);
		container.addInteger(15, 4);
		assertEquals(ConversionUtils.toString(container.array()),"ce8f");
	}
	
	@Test
	public void test11() throws Exception {
		var container = new BitBuffer(ConversionUtils.decodeHexString("e803f3"),Mode.MODE2);
		
		int i = container.getInteger(2);
		assertEquals(i,3);
		i = container.getInteger(2);
		assertEquals(i,2);
		i = container.getInteger(2);
		assertEquals(i,2);
		i = container.getInteger(12);
		assertEquals(i,15);
		i = container.getInteger(2);
		assertEquals(i,3);
		i = container.getInteger(4);
		assertEquals(i,3);
	}
	
	@Test
	public void test12() throws Exception {
		var container = new BitBuffer(ConversionUtils.decodeHexString("e903f3"),Mode.MODE2);
		
		int i = container.getInteger(2);
		assertEquals(i,3);
		i = container.getInteger(2);
		assertEquals(i,2);
		i = container.getInteger(2);
		assertEquals(i,2);
		i = container.getInteger(12);
		assertEquals(i,1039);
		i = container.getInteger(2);
		assertEquals(i,3);
		i = container.getInteger(4);
		assertEquals(i,3);
	}
	@Test
	public void test13() throws Exception {
		var container = new BitBuffer(ConversionUtils.decodeHexString("ea03f3"),Mode.MODE2);
		
		int i = container.getInteger(2);
		assertEquals(i,3);
		i = container.getInteger(2);
		assertEquals(i,2);
		i = container.getInteger(2);
		assertEquals(i,2);
		i = container.getInteger(12);
		assertEquals(i,2063);
		i = container.getInteger(2);
		assertEquals(i,3);
		i = container.getInteger(4);
		assertEquals(i,3);
	}
	
	@Test
	public void test14() throws Exception {
		var container = new BitBuffer(ConversionUtils.decodeHexString("ceff"),Mode.MODE2);
		
		int i = container.getInteger(4);
		assertEquals(i,12);
		i = container.getInteger(2);
		assertEquals(i,3);
		i = container.getInteger(2);
		assertEquals(i,2);
		i = container.getInteger(5);
		assertEquals(i,31);
		i = container.getInteger(3);
		assertEquals(i,7);
	}
	
	@Test
	public void test15() throws Exception {
		var container = new BitBuffer(Mode.MODE2);
		container.addInteger(3, 2);
		container.addInteger(2, 2);
		container.addInteger(2, 2);
		container.addInteger(15, 22);
		container.addInteger(3, 2);
		container.addInteger(3, 4);
		assertEquals(ConversionUtils.toString(container.array()),"e80000fcc0");
	}
	
	@Test
	public void test16() throws Exception {
		var container = new BitBuffer(ConversionUtils.decodeHexString("e80000fcc0"),Mode.MODE2);
		
		int i = container.getInteger(2);
		assertEquals(i,3);
		i = container.getInteger(2);
		assertEquals(i,2);
		i = container.getInteger(2);
		assertEquals(i,2);
		i = container.getInteger(22);
		assertEquals(i,15);
		i = container.getInteger(2);
		assertEquals(i,3);
		i = container.getInteger(4);
		assertEquals(i,3);
	}
	
	@Test
	public void test17() throws Exception {
		var container = new BitBuffer(65535,Mode.MODE2);
		container.addInteger(3, 2);
		container.addInteger(2, 2);
		container.addInteger(2, 2);
		container.addInteger(15, 22);
		container.addInteger(3, 2);
		container.addInteger(3, 4);
		assertEquals(ConversionUtils.toString(container.array()),"e80000fcc0");
	}


}
