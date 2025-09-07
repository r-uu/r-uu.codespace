package de.ruu.lib.ws.rs;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

import static lombok.AccessLevel.PRIVATE;

@Getter @Accessors(fluent = true)
public class ErrorResponse
{
	private String message;
	private String cause;
	private int    httpStatus;

	public ErrorResponse(@NonNull String message)                        { this(message, ""   , 500); }
	public ErrorResponse(@NonNull String message, @NonNull String cause) { this(message, cause, 500); }
	public ErrorResponse(@NonNull String message, @NonNull String cause, int httpStatus)
	{
		this.message    = message;
		this.cause      = cause;
		this.httpStatus = httpStatus;
	}
}