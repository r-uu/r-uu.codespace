package de.ruu.lib.ws.rs;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class ErrorResponseWithException extends ErrorResponse
{
	private SerializableException serializableException;

	public ErrorResponseWithException(@NonNull String message, @NonNull SerializableException serializableException)
	{
		this(message, "", serializableException);
	}

	public ErrorResponseWithException(
			@NonNull String message, @NonNull String cause, @NonNull SerializableException serializableException)
	{
		this(message, cause, 500, serializableException);
	}

	public ErrorResponseWithException(
			@NonNull String message, @NonNull String cause, int httpStatus, @NonNull SerializableException serializableException)
	{
		super(message, cause, httpStatus);
		this.serializableException = serializableException;
	}
}
