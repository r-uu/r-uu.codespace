package de.ruu.app.jeeeraaah.backend.api.ws.rs;

import de.ruu.app.jeeeraaah.common.api.domain.TaskService.TaskRelationException;
import de.ruu.lib.ws.rs.ErrorResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.CONFLICT;
import static jakarta.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

@Provider
@Slf4j
public class ExceptionMapping
{
	@Provider public static class TaskRelationExceptionMapper implements ExceptionMapper<TaskRelationException>
	{
		@Override public Response toResponse(TaskRelationException taskRelationException)
		{
			return
					Response
							.status(CONFLICT)
							.entity(new ErrorResponse("task relation operation failed", taskRelationException.getMessage()))
							.type(APPLICATION_JSON)
							.build();
		}
	}

	/** maps unexpected technical server side errors -> generic JSON error */
	@Provider public static class GenericExceptionMapper implements ExceptionMapper<Throwable>
	{
		@Override public Response toResponse(Throwable t)
		{
			return
					Response
							.status(INTERNAL_SERVER_ERROR)
							.entity(
									new ErrorResponse("INTERNAL_ERROR", t.getMessage(), INTERNAL_SERVER_ERROR.getStatusCode()))
							.type(APPLICATION_JSON)
							.build();
		}
	}
}