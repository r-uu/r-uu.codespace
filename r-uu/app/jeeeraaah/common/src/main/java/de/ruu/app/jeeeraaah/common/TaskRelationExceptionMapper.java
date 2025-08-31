package de.ruu.app.jeeeraaah.common;

import de.ruu.lib.ws.rs.ExceptionDTO;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.CONFLICT;

@Provider public class TaskRelationExceptionMapper implements ExceptionMapper<TaskRelationException>
{
	@Override public Response toResponse(TaskRelationException taskRelationException)
	{
		return
				Response
						.status(CONFLICT)
						.entity(new ExceptionDTO(taskRelationException.getMessage()))
						.type(APPLICATION_JSON)
						.build();
	}
}