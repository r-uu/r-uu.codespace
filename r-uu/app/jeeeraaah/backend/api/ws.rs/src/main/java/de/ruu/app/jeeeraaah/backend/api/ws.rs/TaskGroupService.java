package de.ruu.app.jeeeraaah.backend.api.ws.rs;

import de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskGroupJPA;
import de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskGroupServiceJPA;
import de.ruu.app.jeeeraaah.common.api.domain.TaskGroupLazy;
import de.ruu.app.jeeeraaah.common.api.domain.TaskGroupService.TaskGroupNotFoundException;
import de.ruu.app.jeeeraaah.common.api.domain.TaskService.TaskNotFoundException;
import de.ruu.app.jeeeraaah.common.api.ws.rs.TaskGroupDTO;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
import de.ruu.lib.ws.rs.ErrorResponse;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static de.ruu.app.jeeeraaah.backend.common.mapping.Mappings.toDTO;
import static de.ruu.app.jeeeraaah.backend.common.mapping.Mappings.toJPA;
import static de.ruu.app.jeeeraaah.backend.common.mapping.Mappings.toLazy;
import static de.ruu.app.jeeeraaah.common.api.domain.Paths.ALL_FLAT;
import static de.ruu.app.jeeeraaah.common.api.domain.Paths.BY_ID;
import static de.ruu.app.jeeeraaah.common.api.domain.Paths.BY_ID_LAZY;
import static de.ruu.app.jeeeraaah.common.api.domain.Paths.BY_ID_WITH_TASKS;
import static de.ruu.app.jeeeraaah.common.api.domain.Paths.PATH_APPENDER_TO_DOMAIN_TASK_GROUP;
import static de.ruu.app.jeeeraaah.common.api.domain.Paths.REMOVE_TASK_FROM_GROUP;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;
import static jakarta.ws.rs.core.Response.Status.CREATED;
import static jakarta.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

/**
 * REST controller providing REST endpoints.
 * <p>
 * Methods accepting DTO parameters, transform DTOs to entities, delegate to {@link #service} and transform entity
 * return values from {@link #service} back to DTOs. The transformations from entities to DTOs are
 * intentionally done here after transactions were committed in {@link #service}. This ensures that version
 * attributes of DTOs are respected with their new values after commit in returned DTOs.
 *
 * @author r-uu
 */
@Path(PATH_APPENDER_TO_DOMAIN_TASK_GROUP)
@OpenAPIDefinition(info = @Info(version = "a version", title = "a title"))
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Timed
@Slf4j
public class TaskGroupService
{
	private static final String MSG_TASK_NOT_FOUND               = "task with id %d not found";
	private static final String MSG_TASK_GROUP_NOT_FOUND         = "task group with id %d not found";
	private static final String MSG_TASK_GROUP_READ_FAILED       = "failed to read task group with id %d: %s";
	private static final String MSG_TASK_GROUP_READ_FAILED_XCPTN = "failed to create task group with id %d: %s";
	private static final String MSG_TASK_GROUP_CREATE_FAILED     = "failed to create task group: %s";
	private static final String MSG_TASK_GROUP_UPDATE_FAILED     = "failed to update task group with id %d: %s";
	private static final String MSG_TASK_GROUP_DELETE_FAILED     = "failed to delete task group with id %d: %s";
	private static final String MSG_TASK_REMOVAL_FAILED          = "failed to remove task with id %d from group %d: %s";

	@Inject private TaskGroupServiceJPA service;

	@Operation(summary = "get all task groups", description = "returns a list of all task groups with their details")
	@APIResponse
	(
			responseCode = "200",
			description  = "successfully retrieved all task groups",
			content      = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = TaskGroupDTO.class))
	)
	@GET
	public Response findAll()
	{
		try
		{
			Set<TaskGroupDTO> result  = new HashSet<>();
			ReferenceCycleTracking  context = new ReferenceCycleTracking();
			Set<TaskGroupJPA> all     = service.findAll();

			all.forEach(taskGroup -> result.add(toDTO(taskGroup, context)));

			return Response.ok(result).build();
		}
		catch (Exception e)
		{
			// TODO: use internalServerError
			log.error("error retrieving task groups", e);
			return
					Response.status(INTERNAL_SERVER_ERROR)
							.entity("error retrieving task groups: " + e.getMessage())
							.build();
		}
	}

	@Operation(summary = "get task group by ID", description = "returns a task group by its ID")
	@APIResponse
	(
			responseCode = "200",
			description  = "task group found",
			content      = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = TaskGroupDTO.class))
	)
	@APIResponse
	(
			responseCode = "404",
			description  = "task group not found"
	)
	@GET @Path(BY_ID)
	public Response find
			(@Parameter(description = "ID of the task group", required = true) @PathParam("id") @NonNull Long id)
	{
		try
		{
			return
					service
							.read(id)
							.map
							(
									taskGroup ->
									Response.ok(toDTO(taskGroup, new ReferenceCycleTracking()))
									.build()
							)
							.orElseGet(() -> Response.status(NOT_FOUND).entity(String.format(MSG_TASK_GROUP_NOT_FOUND, id))
							.build());
		}
		catch (Exception e)
		{
			log.error(String.format(MSG_TASK_GROUP_READ_FAILED, id), e);
			return
					Response.status(INTERNAL_SERVER_ERROR)
							.entity(String.format(MSG_TASK_GROUP_READ_FAILED_XCPTN, id, e.getMessage()))
							.build();
		}
	}

	/**
	 * Retrieves a task group by its ID including all associated tasks.
	 *
	 * @param id the ID of the task group to retrieve
	 * @return a {@link Response} containing the task group with its tasks or an error response
	 *
	 * @response 200 Task group found and returned successfully
	 * @response 404 Task group with the specified ID was not found
	 * @response 500 An internal server error occurred
	 */
	@Operation(summary = "get task group with tasks", description = "retrieves a task group with all its associated tasks by ID")
	@APIResponse
	(
			responseCode = "200",
			description  = "Task group found",
			content      = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = TaskGroupDTO.class))
	)
	@APIResponse
	(
			responseCode = "404",
			description  = "Task group not found",
			content      = @Content(schema = @Schema(implementation = ErrorResponse.class))
	)
	@APIResponse
	(
			responseCode = "500",
			description  = "Internal server error",
			content      = @Content(schema = @Schema(implementation = ErrorResponse.class))
	)
	@GET
	@Path(BY_ID_WITH_TASKS)
	public Response findWithTasks
	(
			@Parameter(description = "ID of the task group to retrieve", required = true)
			@PathParam("id") @NonNull Long id
	)
	{
		try
		{
			Optional<TaskGroupJPA> optional = service.findWithTasks(id);

			if (optional.isPresent())
			{
				TaskGroupDTO result = toDTO(optional.get(), new ReferenceCycleTracking());
				return Response.ok(result).build();
			}
			else
			{
				return Response.status(NOT_FOUND).entity(String.format(MSG_TASK_GROUP_NOT_FOUND, id)).build();
			}
		}
		catch (Exception e)
		{
			log.error(String.format(MSG_TASK_GROUP_READ_FAILED, id), e);
			return
					Response.status(INTERNAL_SERVER_ERROR)
							.entity(String.format(MSG_TASK_GROUP_READ_FAILED_XCPTN, id, e.getMessage()))
							.build();
		}
	}

	/**
	 * Retrieves a task group with only the IDs of its related tasks (lazy loading).
	 * This is more efficient when only task references are needed rather than full task details.
	 *
	 * @param id the ID of the task group to retrieve
	 * @return a {@link Response} containing the task group with task IDs or an error response
	 *
	 * @response 200 Task group found and returned successfully
	 * @response 404 Task group with the specified ID was not found
	 * @response 500 An internal server error occurred
	 */
	@Operation(summary = "get task group with task IDs", description = "retrieves a task group with only the IDs of its associated tasks for efficient loading")
	@APIResponse
	(
			responseCode = "200",
			description  = "task group found",
			content      = @Content(mediaType = APPLICATION_JSON,schema = @Schema(implementation = TaskGroupLazy.class))
	)
	@APIResponse
		(
			responseCode = "404",
			description  = "task group not found",
			content      = @Content(schema = @Schema(implementation = ErrorResponse.class))
	)
	@APIResponse
	(
			responseCode = "500",
			description  = "internal server error",
			content      = @Content(schema = @Schema(implementation = ErrorResponse.class))
	)
	@GET @Path(BY_ID_LAZY)
	public Response findWithTasksLazy(@PathParam("id") @NonNull Long id)
	{
		try
		{
			Optional<TaskGroupJPA> optional = service.findWithTasks(id);

			if (optional.isPresent())
			{
				TaskGroupJPA  resultJPA = optional.get();
				TaskGroupLazy result    = toLazy(resultJPA, new ReferenceCycleTracking());
				return Response.ok(result).build();
			}
			else
					return Response.status(NOT_FOUND).entity("company with id " + id + " not found").build();
		}
		catch (Exception e)
		{
			log.error(MSG_TASK_GROUP_READ_FAILED, id, e.getMessage(), e);
			return
					Response.status(INTERNAL_SERVER_ERROR)
							.entity(String.format(MSG_TASK_GROUP_READ_FAILED_XCPTN, id, e.getMessage()))
							.build();
		}
	}

	/**
	 * Retrieves a flat list of all task groups with minimal information.
	 * This is optimized for performance when only basic task group information is needed.
	 *
	 * @return a {@link Response} containing a set of flattened task group information
	 *
	 * @response 200 List of task groups retrieved successfully
	 * @response 500 An internal server error occurred
	 */
	@Operation
	(
			summary     = "get all task groups (flat)",
			description = "retrieves a flat list of all task groups with minimal information for efficient loading"
	)
	@APIResponse
	(
			responseCode = "200",
			description  = "list of task groups retrieved successfully",
			content      = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = TaskGroupDTO.class))
	)
	@APIResponse
	(
			responseCode = "500",
			description  = "internal server error",
			content      = @Content(schema = @Schema(implementation = ErrorResponse.class))
	)
	@GET
	@Path(ALL_FLAT)
	public Response findAllFlat()
	{
		try
		{
			return Response.ok(service.findAllLazy()).build();
		}
		catch (Exception e)
		{
			// TODO: use internalServerError
			log.error("Failed to retrieve flat list of task groups: {}", e.getMessage(), e);
			return
					Response.status(INTERNAL_SERVER_ERROR)
					.entity("Failed to retrieve task groups list: " + e.getMessage())
					.build();
//					internalServerError(e, "Failed to retrieve task groups list");
		}
	}

	@Operation(summary = "create a new task group", description = "creates a new task group with the provided details")
	@APIResponse
	(
			responseCode = "201",
			description  = "ask group created successfully",
			content      =
					@Content
					(
							mediaType = APPLICATION_JSON,
							schema = @Schema(implementation = TaskGroupDTO.class)
					)
	)
	@APIResponse(responseCode = "400", description = "invalid input provided")
	@POST
	public Response create(@Valid @NonNull TaskGroupDTO dto)
	{
		try
		{
			ReferenceCycleTracking context = new ReferenceCycleTracking();
			TaskGroupJPA     entity  = service.create(toJPA(dto, context));
			return
					Response.status(CREATED)
							.entity(toDTO(entity,context))
							.build();
		}
		catch (Exception e)
		{
			log.error("Error creating task group", e);
			return
					Response.status(BAD_REQUEST)
							.entity(String.format(MSG_TASK_GROUP_CREATE_FAILED, e.getMessage()))
							.build();
		}
	}

	@Operation
	(
			summary     = "update a persistent task group or create a new task group",
			description = "updates a persistent task group with the provided details or creates a new task group"
	)
	@APIResponse
	(
			responseCode = "200",
			description  = "persistent task group updated or non-persistent task group created successfully",
			content      =
			@Content
			(
					mediaType = APPLICATION_JSON,
					schema    = @Schema(implementation = TaskGroupDTO.class)
			)
	)
	@APIResponse
	(
			responseCode = "400",
			description  = "invalid input provided"
	)
	@PUT
	public Response update(@Valid @NonNull TaskGroupDTO dto)
	{
		try
		{
			ReferenceCycleTracking context = new ReferenceCycleTracking();
			TaskGroupJPA     entity  = service.update(toJPA(dto, context));
			return Response.ok(toDTO(entity, context)).build();
		}
		catch (TaskGroupNotFoundException e)
		{
			return
					Response.status(NOT_FOUND)
							.entity(String.format(MSG_TASK_GROUP_NOT_FOUND, dto.getId()))
							.build();
		}
		catch (Exception e)
		{
			log.error(String.format("error updating task group with id %d", dto.getId()), e);
			return
					Response.status(BAD_REQUEST)
							.entity(String.format(MSG_TASK_GROUP_UPDATE_FAILED, dto.getId(), e.getMessage()))
							.build();
		}
	}

	@Operation(summary = "delete a task group", description = "deletes a task group by its ID")
	@APIResponse
	(
			responseCode = "200",
			description  = "task group deleted successfully"
	)
	@APIResponse
	(
			responseCode = "404",
			description = "task group not found"
	)
	@DELETE @Path(BY_ID)
	public Response delete(
			@Parameter(description = "ID of the task group to delete", required = true)
			@PathParam("id") @NonNull Long id)
	{
		try
		{
			service.delete(id);
			return Response.ok().build();
		}
		catch (TaskGroupNotFoundException e)
		{
			return
					Response.status(NOT_FOUND)
							.entity(String.format(MSG_TASK_GROUP_NOT_FOUND, id))
							.build();
		}
		catch (Exception e)
		{
			log.error(String.format(MSG_TASK_GROUP_DELETE_FAILED, id, e.getMessage()), e);
			return
					Response.status(INTERNAL_SERVER_ERROR)
							.entity(String.format(MSG_TASK_GROUP_DELETE_FAILED, id, e.getMessage()))
							.build();
		}
	}

	@DELETE @Path(REMOVE_TASK_FROM_GROUP)
	public Response removeFromGroup(
			@PathParam("idGroup") @NonNull Long idGroup, @PathParam("idTask") @NonNull Long idTask)
	{
		try
		{
			service.removeFromGroup(idGroup, idTask);
			return Response.ok().build();
		}
		catch (TaskGroupNotFoundException e)
				{ return Response.status(NOT_FOUND).entity(String.format(MSG_TASK_GROUP_NOT_FOUND, idGroup)).build(); }
		catch (TaskNotFoundException e)
				{ return Response.status(NOT_FOUND).entity(String.format(MSG_TASK_NOT_FOUND      , idTask)).build(); }
		catch (Exception e)
		{
			log.error(String.format(MSG_TASK_REMOVAL_FAILED, idTask, idGroup, e.getMessage()), e);
			return
					Response.status(INTERNAL_SERVER_ERROR)
							.entity(String.format(MSG_TASK_REMOVAL_FAILED, idTask, idGroup, e.getMessage()))
							.build();
		}
	}
}