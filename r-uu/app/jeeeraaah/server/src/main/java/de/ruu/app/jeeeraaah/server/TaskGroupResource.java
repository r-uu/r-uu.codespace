package de.ruu.app.jeeeraaah.server;

import de.ruu.app.jeeeraaah.common.dto.TaskGroupEntityDTO;
import de.ruu.app.jeeeraaah.common.jpa.TaskGroupServiceJPA;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;

import static de.ruu.app.jeeeraaah.common.Paths.PATH_APPENDER_REST_API_TASK_GROUP;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.ok;

@Path(PATH_APPENDER_REST_API_TASK_GROUP)
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@OpenAPIDefinition(info = @Info(version = "a version", title = "a title"))
@Timed
@Slf4j
public class TaskGroupResource
{
	@Inject private TaskGroupServiceJPA service;

	@PUT public Response update(TaskGroupEntityDTO dto)
	{
		// TODO: consider to do basic preparations before doing possibly unnecessary mapping work
//		Task task = taskService.findById(id);
//		if (task == null) {
//			// Application-level 404: Task not found
//			ProblemDetail problem = new ProblemDetail(
//					"https://example.com/errors/task-not-found",
//					"Task not found",
//					404,
//					"No Task with id " + id
//			);
//			return Response.status(Response.Status.NOT_FOUND).entity(problem).build();
//		}
//
//		try {
//			taskService.update(task, dto);
//			return Response.noContent().build(); // 204 success, no body
//		} catch (InvalidUpdateException e) {
//			// Application-level 422: Update failed due to business rule
//			ProblemDetail problem = new ProblemDetail(
//					"https://example.com/errors/invalid-task-update",
//					"Invalid Task update",
//					422,
//					e.getMessage()
//			);
//			return Response.status(422).entity(problem).build();
//		}
		return ok().build();
	}
}