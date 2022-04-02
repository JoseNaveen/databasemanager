package com.joey.databasemanager.protocol.jap;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

@Retention(RUNTIME)
public @interface Length {
	public int size();
	public Unit unit() default Unit.BYTE;
	public int min() default 1;
	public int max() default 65535;
	public Unit payloadUnit() default Unit.BYTE;
}
