package org.helsinki.vismapay.util;

public class BooleanUtils {

	public static Boolean toBoolean(Byte value) {
		return value == null ? null : value == 1;
	}

	public static Byte toByte(Boolean bool) {
		return bool == null ? null : bool ? (byte) 1 : (byte) 0;
	}
}
