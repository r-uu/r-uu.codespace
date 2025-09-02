package de.ruu.lib.util.rs;

import jakarta.ws.rs.core.Response;

public class RestClientCallException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	private Response response;

	public RestClientCallException(String message, Response response)
	{
		super(message);

		this.response = response;
	}

	public Response getResponse()
	{
		return response;
	}
}