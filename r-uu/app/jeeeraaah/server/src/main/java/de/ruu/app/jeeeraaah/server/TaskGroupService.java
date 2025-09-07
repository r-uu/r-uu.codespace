package de.ruu.app.jeeeraaah.server;

import de.ruu.app.jeeeraaah.common.TaskGroupService.TaskGroupNotFoundException;
import de.ruu.app.jeeeraaah.common.TaskService;
import de.ruu.app.jeeeraaah.common.TaskService.TaskNotFoundException;
import de.ruu.app.jeeeraaah.common.dto.TaskGroupEntityDTO;
import de.ruu.app.jeeeraaah.common.dto.TaskGroupFlat;
import de.ruu.app.jeeeraaah.common.dto.TaskGroupLazy;
import de.ruu.app.jeeeraaah.common.jpa.TaskGroupEntityJPA;
import de.ruu.app.jeeeraaah.common.jpa.TaskGroupServiceJPA;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
import jakarta.inject.Inject;
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
import org.eclipse.microprofile.openapi.annotations.info.Info;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static de.ruu.app.jeeeraaah.common.Paths.ALL_FLAT;
import static de.ruu.app.jeeeraaah.common.Paths.BY_ID;
import static de.ruu.app.jeeeraaah.common.Paths.BY_ID_LAZY;
import static de.ruu.app.jeeeraaah.common.Paths.BY_ID_WITH_TASKS;
import static de.ruu.app.jeeeraaah.common.Paths.PATH_APPENDER_TO_DOMAIN_TASK_GROUP;
import static de.ruu.app.jeeeraaah.common.Paths.REMOVE_TASK_FROM_GROUP;
import static de.ruu.lib.util.BooleanFunctions.not;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.CREATED;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;
import static jakarta.ws.rs.core.Response.ok;
import static jakarta.ws.rs.core.Response.status;

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
// TODO fix exception handling
@Path(PATH_APPENDER_TO_DOMAIN_TASK_GROUP)
@OpenAPIDefinition(info = @Info(version = "a version", title = "a title"))
@Timed
@Slf4j
public class TaskGroupService
{
	@Inject private TaskGroupServiceJPA service;

	@GET
	@Produces(APPLICATION_JSON)
	public Response findAll()
	{
		Set<TaskGroupEntityDTO> result  = new HashSet<>();
		ReferenceCycleTracking  context = new ReferenceCycleTracking();
		Set<TaskGroupEntityJPA> all     = service.findAll();

		for (TaskGroupEntityJPA taskGroupEntity : all)
		{
			TaskGroupEntityDTO dto = taskGroupEntity.toDTO(context);
			result.add(dto);
//			log.debug("found task group {}", dto);
		}

		return ok(result).build();
	}

	@GET
	@Path(BY_ID)
	@Produces(APPLICATION_JSON)
	public Response find(@PathParam("id") Long id)
	{
		Optional<? extends TaskGroupEntityJPA> optional = service.read(id);
		if (not(optional.isPresent()))
				return status(NOT_FOUND).entity("task group with id " + id + " not found").build();
		else
		{
			ReferenceCycleTracking context = new ReferenceCycleTracking();

			TaskGroupEntityDTO result = optional.get().toDTO(context);
//			log.debug("found task group for id {}\n{}", id, result);
			return ok(result).build();
		}
	}

	@GET
	@Path(BY_ID_WITH_TASKS)
	@Produces(APPLICATION_JSON)
	public Response findWithTasks(@PathParam("id") Long id)
	{
		Optional<? extends TaskGroupEntityJPA> optional = service.findWithTasks(id);

		if (not(optional.isPresent()))
				return status(NOT_FOUND).entity("company with id " + id + " not found").build();
		else
		{
			ReferenceCycleTracking context = new ReferenceCycleTracking();

			TaskGroupEntityJPA resultJPA = optional.get();
			TaskGroupEntityDTO result    = resultJPA.toDTO(context);
			return ok(result).build();
		}
	}

	/**
	 * This method is used to find task groups together with the ids of its related tasks.
	 *
	 * @param id the id of the task group
	 * @return the task group with ids of related tasks
	 */
	@GET
	@Path(BY_ID_LAZY)
	@Produces(APPLICATION_JSON)
	public Response findWithTasksLazy(@PathParam("id") Long id)
	{
		Optional<TaskGroupEntityJPA> optional = service.findWithTasks(id);

		if (not(optional.isPresent()))
				return status(NOT_FOUND).entity("company with id " + id + " not found").build();
		else
		{
			TaskGroupEntityJPA resultJPA = optional.get();
			TaskGroupLazy      result    = resultJPA.toLazy();
			return ok(result).build();
		}
	}

	@GET
	@Path(ALL_FLAT)
	@Produces(APPLICATION_JSON)
	public Response findAllFlat()
	{
		@NonNull Set<TaskGroupFlat> result = service.findAllFlat();
		return ok(result).build();
	}

	@POST
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public Response create(TaskGroupEntityDTO dto)
	{
		ReferenceCycleTracking context = new ReferenceCycleTracking();

//		log.debug( "input:\n{}", dto);
		TaskGroupEntityJPA entity = service.create(dto.toEntity(context));
		TaskGroupEntityDTO result = entity.toDTO(context);
//		log.debug("output:\n{}", result);
		return status(CREATED).entity(result).build();
	}

	@PUT
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public Response update(TaskGroupEntityDTO dto)
	{
		ReferenceCycleTracking context = new ReferenceCycleTracking();

		TaskGroupEntityJPA entity = service.update(dto.toEntity(context));
		TaskGroupEntityDTO result = entity.toDTO(context);
		return ok(result).build();
	}

	@DELETE
	@Path(BY_ID)
	@Produces(APPLICATION_JSON)
	public Response delete(@PathParam("id") Long id)
	{
		try
		{
			service.delete(id);
		}
		catch (TaskGroupNotFoundException e)
		{
			return status(NOT_FOUND).entity("task group with id " + id + " not found").build();
		}

		return ok().build();
	}

	@DELETE
	@Path(REMOVE_TASK_FROM_GROUP)
	@Produces(APPLICATION_JSON)
	public Response removeFromGroup(@PathParam("idGroup") Long idGroup, @PathParam("idTask") Long idTask)
	{
		try
		{
			service.removeFromGroup(idGroup, idTask);
		}
		catch (TaskGroupNotFoundException e)
		{
			return status(NOT_FOUND).entity("task group with id " + idGroup + "not found").build();
		}
		catch (TaskNotFoundException e)
		{
			return status(NOT_FOUND).entity("task with id " + idTask + " not found").build();
		}

		return ok().build();
	}
}