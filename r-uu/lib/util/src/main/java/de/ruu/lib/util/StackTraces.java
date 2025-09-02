package de.ruu.lib.util;

import java.util.Arrays;

public class StackTraces
{
	public static String toString(Throwable t) { return Arrays.toString(t.getStackTrace()).replaceAll(", ", "\n"); }
}
