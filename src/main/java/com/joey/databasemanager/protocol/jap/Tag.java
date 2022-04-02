package com.joey.databasemanager.protocol.jap;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

@Retention(RUNTIME)
public @interface Tag {
	public int value();
	public int size() default 1;
	public Unit unit() default Unit.BYTE;
}
