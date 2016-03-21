package com.rekoe.utils;

import java.util.Random;

public class CharGenerator {
	private static Random r = new Random();
	private CharGenerator() {
	}

	private static final char[] src = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

	public static char next() {
		return src[Math.abs(r.nextInt(src.length))];
	}
}
