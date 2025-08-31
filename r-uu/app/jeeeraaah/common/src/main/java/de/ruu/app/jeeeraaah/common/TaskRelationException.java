package de.ruu.app.jeeeraaah.common;

public class TaskRelationException extends RuntimeException
{
	public TaskRelationException(String message) { super(message); }
	public String message() { return super.getMessage(); }
}