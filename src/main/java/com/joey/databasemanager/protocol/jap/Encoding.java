package com.joey.databasemanager.protocol.jap;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
public @interface Encoding {
	public int size() default 32;
	public Unit unit() default Unit.BIT;
}
