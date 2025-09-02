package de.ruu.app.jeeeraaah.server;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

@Provider
@Slf4j
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException>
{
	@Override public Response toResponse(NotFoundException e)
	{
		log.error("resource could not be found", e);
		return Response.status(NOT_FOUND).build();
	}
}