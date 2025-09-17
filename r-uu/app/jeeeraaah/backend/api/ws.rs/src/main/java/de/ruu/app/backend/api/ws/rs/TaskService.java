package de.ruu.app.backend.api.ws.rs;

import de.ruu.app.jeeeraaah.common.api.domain.InterTaskRelationData;
import de.ruu.app.jeeeraaah.common.RemoveNeighboursFromTaskConfig;
import de.ruu.app.jeeeraaah.common.api.domain.TaskCreationData;
import de.ruu.app.jeeeraaah.common.TaskService.TaskRelationException;
import de.ruu.app.jeeeraaah.common.dto.TaskEntityDTO;
import de.ruu.app.jeeeraaah.common.dto.TaskGroupEntityDTO;
import de.ruu.app.jeeeraaah.common.dto.TaskLazy;
import de.ruu.app.jeeeraaah.common.jpa.TaskEntityJPA;
import de.ruu.app.jeeeraaah.common.jpa.TaskGroupEntityJPA;
import de.ruu.app.jeeeraaah.common.jpa.TaskGroupServiceJPA;
import de.ruu.app.jeeeraaah.common.jpa.TaskServiceJPA;
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
import static jakarta.ws.rs.core.Response.ok;
import static jakarta.ws.rs.core.Response.status;

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
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
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
		Optional<? extends TaskGroupEntityJPA> optional = taskGroupService.read(data.taskGroupId());
		if (BooleanFunctions.not(optional.isPresent()))
				return Response.status(Status.NOT_FOUND).entity("task group with id " + data.taskGroupId() + " not found").build();
		TaskGroupEntityJPA taskGroupEntityJPAPersistent = optional.get();
		// TODO find a way to avoid jpa -> dto -> jpa -> dto
		TaskGroupEntityDTO taskGroupEntityDTOPersistent = taskGroupEntityJPAPersistent.toDTO(new ReferenceCycleTracking());
		// complete preparation of task dto with task group dto, otherwise mapping will fail because the task has no task group
		data.task().taskGroup(taskGroupEntityDTOPersistent);
		// map task dto in data to task jpa
		TaskEntityJPA taskEntityJPATransient = data.task().toEntity(new ReferenceCycleTracking());
		// update task jpa with persistent task group
		taskEntityJPATransient.taskGroup(taskGroupEntityJPAPersistent);
		// persist task jpa
		TaskEntityJPA taskEntityJPAPersistent = taskService.create(taskEntityJPATransient);
		TaskEntityDTO result                  = taskEntityJPAPersistent.toDTO(new ReferenceCycleTracking());
		return Response.status(Status.CREATED).entity(result).build();
	}

	@PUT public Response update(TaskEntityDTO dto)
	{
		ReferenceCycleTracking context = new ReferenceCycleTracking();

		TaskEntityJPA entity = taskService.update(dto.toEntity(context));
		TaskEntityDTO result = entity.toDTO(context);
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
			return Response.status(Status.CONFLICT).build();
		}
		return Response.ok().build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAll()
	{
		Set<TaskEntityDTO> result = new HashSet<>();

		ReferenceCycleTracking context = new ReferenceCycleTracking();

		for (TaskEntityJPA taskEntityJPA : taskService.findAll())
		{
			TaskEntityDTO dto = taskEntityJPA.toDTO(context);
			result.add(dto);
			log.debug("found task {}", dto);
		}

		return Response.ok(result).build();
	}

	@GET
	@Path(BY_ID)
	@Produces(MediaType.APPLICATION_JSON)
	public Response find(@PathParam("id") Long id)
	{
		Optional<? extends TaskEntityJPA> optional = taskService.read(id);
		if (BooleanFunctions.not(optional.isPresent()))
				return Response.status(Status.NOT_FOUND).entity("task with id " + id + " not found").build();
		else
		{
			ReferenceCycleTracking context = new ReferenceCycleTracking();

			TaskEntityDTO result = optional.get().toDTO(context);
			log.debug("found task for id {}\n{}", id, result);
			return Response.ok(result).build();
		}
	}

	@GET
	@Path(BY_ID_WITH_RELATED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findWithRelated(@PathParam("id") Long id)
	{
		Optional<? extends TaskEntityJPA> optional = taskService.findWithRelated(id);
		if (BooleanFunctions.not(optional.isPresent()))
				return Response.status(Status.NOT_FOUND).entity("task with id " + id + " not found").build();
		else
		{
			ReferenceCycleTracking context = new ReferenceCycleTracking();
			TaskEntityDTO          result  = optional.get().toDTO(context);
			return Response.ok(result).build();
		}
	}

	@GET
	@Path(BY_ID_WITH_RELATED_LAZY)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findWithRelatedLazy(@PathParam("id") Long id)
	{
		Optional<TaskEntityJPA> optional = taskService.findWithRelated(id);
		if (BooleanFunctions.not(optional.isPresent()))
			return Response.status(Status.NOT_FOUND).entity("task with id " + id + " not found").build();
		else
		{
			TaskEntityJPA resultJPA = optional.get();
			TaskLazy      result    = resultJPA.toLazy();
			return Response.ok(result).build();
		}
	}

	@POST // use POST for bulk operations
	@Path(BY_IDS_WITH_RELATED_LAZY)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findWithRelatedLazy(Set<Long> ids)
	{
		Set<TaskLazy> result = taskService.findTasksLazy(ids);
		return Response.ok(result).build();
	}

	@PUT
	@Path(ADD_SUB)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addSubTask(@NonNull InterTaskRelationData data)
	{
		log.debug("id task: {}, id sub task: {}", data.id(), data.idRelated());
		taskService.addSubTask(data.id(), data.idRelated());
		return Response.ok().build();
	}

	@PUT
	@Path(ADD_PREDECESSOR)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
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
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
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
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
//	public Response removeSubTask(@PathParam("idTask") Long idTask, @PathParam("idSubTask") Long idSubTask)
	public Response removeSubTask(@NonNull InterTaskRelationData data)
	{
		log.debug("id task: {}, id sub task: {}", data.id(), data.idRelated());
		try { taskService.removeSubTask(data.id(), data.idRelated()); }
		catch (NotFoundException e) { return Response.status(Status.NOT_FOUND).entity(e.getMessage()).build(); }
		return Response.ok().build();
	}

	@PUT
	@Path(REMOVE_PREDECESSOR)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
//	public Response removePredecessor(@PathParam("idTask") Long idTask, @PathParam("idPredecessor") Long idPredecessor)
	public Response removePredecessor(@NonNull InterTaskRelationData data)
	{
		log.debug("id task: {}, id predecessor task: {}", data.id(), data.idRelated());
		try { taskService.removePredecessor(data.id(), data.idRelated()); }
		catch (NotFoundException e) { return Response.status(Status.NOT_FOUND).entity(e.getMessage()).build(); }
		return Response.ok().build();
	}

	@PUT
	@Path(REMOVE_SUCCESSOR)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeSuccessor(@NonNull InterTaskRelationData data)
	{
		log.debug("id task: {}, id successor task: {}", data.id(), data.idRelated());
		try { taskService.removeSuccessor(data.id(), data.idRelated()); }
		catch (NotFoundException e) { return Response.status(Status.NOT_FOUND).entity(e.getMessage()).build(); }
		return Response.ok().build();
	}

	@PUT
	@Path(REMOVE_NEIGHBOURS_FROM_TASK)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeNeighboursFromTask(@NonNull RemoveNeighboursFromTaskConfig data)
	{
		log.debug("removing from neighbours from task with id: {}", data.idTask());
		try { taskService.removeNeighboursFromTask(data); }
		catch (NotFoundException e) { return Response.status(Status.NOT_FOUND).entity(e.getMessage()).build(); }
		return Response.ok().build();
	}
}