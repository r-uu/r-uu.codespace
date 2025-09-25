package de.ruu.app.jeeeraaah.backend.api.ws.rs;

import de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskGroupJPA;
import de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskGroupServiceJPA;
import de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskJPA;
import de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskServiceJPA;
import de.ruu.app.jeeeraaah.common.api.domain.InterTaskRelationData;
import de.ruu.app.jeeeraaah.common.api.domain.RemoveNeighboursFromTaskConfig;
import de.ruu.app.jeeeraaah.common.api.domain.TaskCreationData;
import de.ruu.app.jeeeraaah.common.api.domain.TaskLazy;
import de.ruu.app.jeeeraaah.common.api.domain.TaskService.TaskRelationException;
import de.ruu.app.jeeeraaah.common.api.ws.rs.TaskDTO;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
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

import static de.ruu.app.jeeeraaah.backend.common.mapping.Mappings.toDTO;
import static de.ruu.app.jeeeraaah.backend.common.mapping.Mappings.toJPA;
import static de.ruu.app.jeeeraaah.backend.common.mapping.Mappings.toLazy;
import static de.ruu.app.jeeeraaah.common.api.domain.Paths.ADD_PREDECESSOR;
import static de.ruu.app.jeeeraaah.common.api.domain.Paths.ADD_SUB;
import static de.ruu.app.jeeeraaah.common.api.domain.Paths.ADD_SUCCESSOR;
import static de.ruu.app.jeeeraaah.common.api.domain.Paths.BY_ID;
import static de.ruu.app.jeeeraaah.common.api.domain.Paths.BY_IDS_WITH_RELATED_LAZY;
import static de.ruu.app.jeeeraaah.common.api.domain.Paths.BY_ID_WITH_RELATED;
import static de.ruu.app.jeeeraaah.common.api.domain.Paths.BY_ID_WITH_RELATED_LAZY;
import static de.ruu.app.jeeeraaah.common.api.domain.Paths.PATH_APPENDER_TO_DOMAIN_TASK;
import static de.ruu.app.jeeeraaah.common.api.domain.Paths.REMOVE_NEIGHBOURS_FROM_TASK;
import static de.ruu.app.jeeeraaah.common.api.domain.Paths.REMOVE_PREDECESSOR;
import static de.ruu.app.jeeeraaah.common.api.domain.Paths.REMOVE_SUB;
import static de.ruu.app.jeeeraaah.common.api.domain.Paths.REMOVE_SUCCESSOR;
import static de.ruu.lib.util.BooleanFunctions.not;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.CONFLICT;
import static jakarta.ws.rs.core.Response.Status.CREATED;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

/**
 * REST controller providing REST endpoints.
 * <p>
 * Methods accept DTO parameters, transform DTOs to entities, delegate to {@link #taskService} and transform entity
 * return values from {@link #taskService} back to DTOs. The transformations from entities to DTOs are intentionally
 * done here after transactions were committed in {@link #taskService}. This ensures that version attributes of DTOs
 * are respected with their new values after commit in returned DTOs.
 *
 * @author r-uu
 */
// TODO fix exception handling
@RequestScoped
@Path(PATH_APPENDER_TO_DOMAIN_TASK)
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@OpenAPIDefinition(info = @Info(version = "a version", title = "a title"))
@Timed
@Slf4j
public class TaskService
{
	@Inject private TaskGroupServiceJPA taskGroupService;
	@Inject private TaskServiceJPA      taskService;

	@POST public Response create(TaskCreationData data)
	{
		// find persistent task group jpa from task group id in data
		Optional<? extends TaskGroupJPA> optional = taskGroupService.read(data.taskGroupId());
		if (not(optional.isPresent()))
				return Response.status(NOT_FOUND).entity("task group with id " + data.taskGroupId() + " not found").build();
		TaskGroupJPA taskGroupJPAPersistent = optional.get();
		// map lazy task dto in data to task jpa
		TaskJPA taskJPATransient = toJPA(taskGroupJPAPersistent, data.task());
		// update task jpa with persistent task group
		taskJPATransient.taskGroup(taskGroupJPAPersistent);
		// store task in backend
		TaskJPA taskJPAPersistent = taskService.create(taskJPATransient);
		TaskDTO result            = toDTO(taskJPAPersistent, new ReferenceCycleTracking());
		return Response.status(CREATED).entity(result).build();
	}

	@PUT public Response update(TaskDTO dto)
	{
		ReferenceCycleTracking context = new ReferenceCycleTracking();

		TaskJPA entity = taskService.update(toJPA(dto, context));
		TaskDTO result = toDTO(entity, context);
		return Response.ok(result).build();
	}

	@DELETE @Path(BY_ID) public Response delete(@PathParam("id") Long id)
	{
		try
		{
			taskService.delete(id);
		}
		catch (Exception e)
		{
			return Response.status(CONFLICT).build();
		}
		return Response.ok().build();
	}

	@GET
	@Produces(APPLICATION_JSON)
	public Response findAll()
	{
		Set<TaskDTO> result = new HashSet<>();

		ReferenceCycleTracking context = new ReferenceCycleTracking();

		for (TaskJPA taskJPA : taskService.findAll())
		{
			TaskDTO dto = toDTO(taskJPA,context);
			result.add(dto);
			log.debug("found task {}", dto);
		}

		return Response.ok(result).build();
	}

	@GET
	@Path(BY_ID)
	@Produces(APPLICATION_JSON)
	public Response find(@PathParam("id") Long id)
	{
		Optional<? extends TaskJPA> optional = taskService.read(id);
		if (not(optional.isPresent()))
				return Response.status(NOT_FOUND).entity("task with id " + id + " not found").build();
		else
		{
			ReferenceCycleTracking context = new ReferenceCycleTracking();

			TaskDTO result = toDTO(optional.get(), context);
			log.debug("found task for id {}\n{}", id, result);
			return Response.ok(result).build();
		}
	}

	@GET
	@Path(BY_ID_WITH_RELATED)
	@Produces(APPLICATION_JSON)
	public Response findWithRelated(@PathParam("id") Long id)
	{
		Optional<? extends TaskJPA> optional = taskService.findWithRelated(id);
		if (not(optional.isPresent()))
				return Response.status(NOT_FOUND).entity("task with id " + id + " not found").build();
		else
		{
			TaskDTO result  = toDTO(optional.get(), new ReferenceCycleTracking());
			return Response.ok(result).build();
		}
	}

	@GET
	@Path(BY_ID_WITH_RELATED_LAZY)
	@Produces(APPLICATION_JSON)
	public Response findWithRelatedLazy(@PathParam("id") Long id)
	{
		Optional<TaskJPA> optional = taskService.findWithRelated(id);
		if (not(optional.isPresent()))
				return Response.status(NOT_FOUND).entity("task with id " + id + " not found").build();
		else
		{
			TaskJPA  resultJPA = optional.get();
			TaskLazy result    = toLazy(resultJPA, new ReferenceCycleTracking());
			return Response.ok(result).build();
		}
	}

	@POST // use POST for bulk operations
	@Path(BY_IDS_WITH_RELATED_LAZY)
	@Produces(APPLICATION_JSON)
	public Response findWithRelatedLazy(Set<Long> ids)
	{
		Set<TaskLazy> result = new HashSet<>();
		taskService.findTasks(ids).forEach(t -> result.add(toLazy(t, new ReferenceCycleTracking())));
		return Response.ok(result).build();
	}

	@PUT
	@Path(ADD_SUB)
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public Response addSubTask(@NonNull InterTaskRelationData data)
	{
		log.debug("id task: {}, id sub task: {}", data.id(), data.idRelated());
		taskService.addSubTask(data.id(), data.idRelated());
		return Response.ok().build();
	}

	@PUT
	@Path(ADD_PREDECESSOR)
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public Response addPredecessor(@NonNull InterTaskRelationData data)
	{
		try { taskService.addPredecessor(data.id(), data.idRelated()); }
		catch (Throwable t)
		{
			log.debug("throwable caught: {}", t.getMessage());
			if (t instanceof TaskRelationException)
					log.debug("it is a TaskRelationException");
			else
					log.debug("it is not a TaskRelationException");
			throw t;
		}
//		try { taskService.addPredecessor(data.id(), data.idRelated()); }
//		catch (NotFoundException e)
//				{ return status(NOT_FOUND).entity(e.getMessage()).build(); }
////		catch (TaskRelationException e) // do not catch here, let it be mapped by TaskRelationExceptionMapper
////				{ return status(BAD_REQUEST).entity(e.getMessage()).build(); }
		return Response.ok().build();
	}

	@PUT
	@Path(ADD_SUCCESSOR)
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public Response addSuccessor(@NonNull InterTaskRelationData data)
	{
		log.debug("id task: {}, id successor task: {}", data.id(), data.idRelated());
		taskService.addSuccessor(data.id(), data.idRelated());
//		try { taskService.addSuccessor(data.id(), data.idRelated()); }
//		catch (NotFoundException e) { return status(NOT_FOUND).entity(e.getMessage()).build(); }
////		catch (TaskRelationException e) // do not catch here, let it be mapped by TaskRelationExceptionMapper
////				{ return status(BAD_REQUEST).entity(e.getMessage()).build(); }
		return Response.ok().build();
	}

	@PUT
	@Path(REMOVE_SUB)
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
//	public Response removeSubTask(@PathParam("idTask") Long idTask, @PathParam("idSubTask") Long idSubTask)
	public Response removeSubTask(@NonNull InterTaskRelationData data)
	{
		log.debug("id task: {}, id sub task: {}", data.id(), data.idRelated());
		try { taskService.removeSubTask(data.id(), data.idRelated()); }
		catch (NotFoundException e) { return Response.status(NOT_FOUND).entity(e.getMessage()).build(); }
		return Response.ok().build();
	}

	@PUT
	@Path(REMOVE_PREDECESSOR)
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
//	public Response removePredecessor(@PathParam("idTask") Long idTask, @PathParam("idPredecessor") Long idPredecessor)
	public Response removePredecessor(@NonNull InterTaskRelationData data)
	{
		log.debug("id task: {}, id predecessor task: {}", data.id(), data.idRelated());
		try { taskService.removePredecessor(data.id(), data.idRelated()); }
		catch (NotFoundException e) { return Response.status(NOT_FOUND).entity(e.getMessage()).build(); }
		return Response.ok().build();
	}

	@PUT
	@Path(REMOVE_SUCCESSOR)
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public Response removeSuccessor(@NonNull InterTaskRelationData data)
	{
		log.debug("id task: {}, id successor task: {}", data.id(), data.idRelated());
		try { taskService.removeSuccessor(data.id(), data.idRelated()); }
		catch (NotFoundException e) { return Response.status(NOT_FOUND).entity(e.getMessage()).build(); }
		return Response.ok().build();
	}

	@PUT
	@Path(REMOVE_NEIGHBOURS_FROM_TASK)
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public Response removeNeighboursFromTask(@NonNull RemoveNeighboursFromTaskConfig data)
	{
		log.debug("removing from neighbours from task with id: {}", data.idTask());
		try { taskService.removeNeighboursFromTask(data); }
		catch (NotFoundException e) { return Response.status(NOT_FOUND).entity(e.getMessage()).build(); }
		return Response.ok().build();
	}
}