package com.demo.model;

public record ProcessInfo(
		long pid,
		String user,
		String command,
		String arguments,
		String startTime,
		long startEpochMillis,
		boolean alive
) {
}

