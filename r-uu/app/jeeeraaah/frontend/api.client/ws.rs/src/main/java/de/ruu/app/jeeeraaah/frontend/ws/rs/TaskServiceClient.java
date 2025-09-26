package de.ruu.app.jeeeraaah.frontend.ws.rs;

import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import de.ruu.app.jeeeraaah.common.api.bean.TaskBean;
import de.ruu.app.jeeeraaah.common.api.domain.InterTaskRelationData;
import de.ruu.app.jeeeraaah.common.api.domain.Paths;
import de.ruu.app.jeeeraaah.common.api.domain.RemoveNeighboursFromTaskConfig;
import de.ruu.app.jeeeraaah.common.api.domain.TaskCreationData;
import de.ruu.app.jeeeraaah.common.api.domain.TaskLazy;
import de.ruu.app.jeeeraaah.common.api.domain.TaskService;
import de.ruu.app.jeeeraaah.common.api.domain.TaskServiceLazy;
import de.ruu.app.jeeeraaah.common.api.ws.rs.TaskDTO;
import de.ruu.lib.cdi.common.CDIUtil;
import de.ruu.lib.cdi.se.EventDispatcher;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
import de.ruu.lib.util.AbstractEvent;
import de.ruu.lib.ws.rs.ErrorResponse;
import de.ruu.lib.ws.rs.NonTechnicalException;
import de.ruu.lib.ws.rs.TechnicalException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.ConfigProvider;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static de.ruu.app.jeeeraaah.common.api.mapping.Mappings.toBean;
import static de.ruu.app.jeeeraaah.common.api.mapping.Mappings.toDTO;
import static de.ruu.app.jeeeraaah.common.api.mapping.Mappings.toLazy;
import static jakarta.ws.rs.client.Entity.entity;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.Family.SUCCESSFUL;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

@Singleton
@Slf4j
public class TaskServiceClient implements TaskService<TaskBean>, TaskServiceLazy
{
	private static final String UNEXPECTED_STATUS = "unexpected status: ";

	private final String  scheme =
			ConfigProvider.getConfig().getOptionalValue("jeeeraaah.rest-api.scheme", String.class).orElse("http");

	private final String  host   =
			ConfigProvider.getConfig().getOptionalValue("jeeeraaah.rest-api.host"  , String.class).orElse("127.0.0.1");

	private final Integer port   =
			ConfigProvider.getConfig().getOptionalValue("jeeeraaah.rest-api.port"  , Integer.class).orElse(9080);

	private Client client;

	private WebTarget webTarget;

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// lifecycle methods
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@PostConstruct public void postConstruct()
	{
		String schemaHostPort = scheme + "://" + host + ":" + port;

		URI uri = URI.create(schemaHostPort + Paths.PATH_TO_APP + Paths.PATH_APPENDER_TO_DOMAIN_TASK);

		log.debug("scheme        : {}", scheme);
		log.debug("host          : {}", host);
		log.debug("port          : {}", port);
		log.debug("schemaHostPort: {}", schemaHostPort);
		log.debug("uri           : {}", uri);

		client =
				ClientBuilder
						.newBuilder()
						.register(JacksonJsonProvider.class) // register Jackson to (de)serialize JSON
						.build();

		webTarget = client.target(uri);

		List<String> classNames = new ArrayList<>();
		client.getConfiguration().getClasses().forEach(c -> classNames.add(c.getName()));
		log.debug("rs client configuration classes\n{}", String.join("\n", classNames));
	}

	@PreDestroy public void preDestroy() { client.close(); }

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// interface implementations
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override public @NonNull TaskBean create(@NonNull TaskBean task) throws TechnicalException, NonTechnicalException
	{
		try
		{
			Response response =
					webTarget
							.request()
							.post(
									entity(
											new TaskCreationData(
													requireNonNull(task.taskGroup().id()),
													toLazy(task, new ReferenceCycleTracking())), APPLICATION_JSON));

			if (response.getStatusInfo().getFamily() == SUCCESSFUL)
			{
				TaskDTO  dto    = response.readEntity(TaskDTO.class);
				TaskBean result = toBean(dto, new ReferenceCycleTracking());
				// fire event to indicate that a new task has been created in the backend
				CDIUtil.fire(new TaskCreatedInBackendEvent(this, result));
				return result;
			}
			else
					// handle business error (payload known)
					throw new NonTechnicalException(response.readEntity(ErrorResponse.class));
		}
		catch (ProcessingException e)
		{
			// this is thrown for technical issues (server down, wrong URL, timeout)
			throw new TechnicalException("communication error", e);
		}
	}

	@Override public Optional<TaskBean> read(@NonNull Long id) throws TechnicalException, NonTechnicalException
	{
		try
		{
			Response response = webTarget.resolveTemplate("id", id).request().get();
			int      status   = response.getStatus();

			if (status == Status.OK.getStatusCode())
			{
				return Optional.of(toBean(response.readEntity(TaskDTO.class), new ReferenceCycleTracking()));
			}
			else if (status == Status.NOT_FOUND.getStatusCode())
			{
				return Optional.empty();
			}
			else
					// handle business error (payload known)
					throw new NonTechnicalException(response.readEntity(ErrorResponse.class));
		}
		catch (ProcessingException e)
		{
			// this is thrown for technical issues (server down, wrong URL, timeout)
			throw new TechnicalException("communication error", e);
		}
	}

	@Override public @NonNull TaskBean update(@NonNull TaskBean task) throws TechnicalException, NonTechnicalException
	{
		try
		{
			Response response =
					webTarget
							.request()
							.put(entity(toDTO(task, new ReferenceCycleTracking()), APPLICATION_JSON));

			if (response.getStatusInfo().getFamily() == SUCCESSFUL)
			{
				TaskBean result = toBean(response.readEntity(TaskDTO.class), new ReferenceCycleTracking());
				// fire event to indicate that a new task has been created in the backend
				CDIUtil.fire(new TaskUpdatedInBackendEvent(this, result));
				return result;
			}
			else
					// handle business error (payload known)
					throw new NonTechnicalException(response.readEntity(ErrorResponse.class));
		}
		catch (ProcessingException e)
		{
			// this is thrown for technical issues (server down, wrong URL, timeout)
			throw new TechnicalException("communication error", e);
		}
	}

	@Override public void delete(@NonNull Long id) throws TechnicalException, NonTechnicalException
	{
		try
		{
			Response response = webTarget.resolveTemplate("id", id).request().delete();

			if (response.getStatusInfo().getFamily() == SUCCESSFUL)
			{
				// fire event to indicate that a new task has been created in the backend
				CDIUtil.fire(new TaskDeletedInBackendEvent(this, id));
			}
			else
					// handle business error (payload known)
					throw new NonTechnicalException(response.readEntity(ErrorResponse.class));
		}
		catch (ProcessingException e)
		{
			// this is thrown for technical issues (server down, wrong URL, timeout)
			throw new TechnicalException("communication error", e);
		}
	}

	@Override public Set<TaskBean> findAll() throws TechnicalException, NonTechnicalException
	{
		try
		{
			Response response = webTarget.request().get();

			if (response.getStatusInfo().getFamily() == SUCCESSFUL)
			{
				return
						response
								.readEntity(new GenericType<HashSet<TaskDTO>>() { })
								.stream()
								.map(t -> toBean((t), new ReferenceCycleTracking()))
								.collect(Collectors.toSet());
			}
			else
					// handle business error (payload known)
					throw new NonTechnicalException(response.readEntity(ErrorResponse.class));
		}
		catch (ProcessingException e)
		{
			// this is thrown for technical issues (server down, wrong URL, timeout)
			throw new TechnicalException("communication error", e);
		}
	}

	@Override public Optional<TaskBean> findWithRelated(@NonNull Long id) throws TechnicalException, NonTechnicalException
	{
		try
		{
			WebTarget target   = client.target(webTarget.getUri() + Paths.BY_ID_WITH_RELATED);
			Response  response = target.resolveTemplate("id", id).request().get();

			if (response.getStatusInfo().getFamily() == SUCCESSFUL)
			{
				TaskDTO taskDTO = response.readEntity(TaskDTO.class);
				return Optional.of(toBean(taskDTO, new ReferenceCycleTracking()));
			}
			else
					// handle business error (payload known)
					throw new NonTechnicalException(response.readEntity(ErrorResponse.class));
		}
		catch (ProcessingException e)
		{
			// this is thrown for technical issues (server down, wrong URL, timeout)
			throw new TechnicalException("communication error", e);
		}
	}

	@Override public Set<? extends TaskBean> findTasks(@NonNull Set<Long> ids) throws Exception
	{
		try
		{
			WebTarget target   = client.target(webTarget.getUri() + Paths.BY_IDS_WITH_RELATED_LAZY);
			Response  response = target.request(APPLICATION_JSON).post(entity(ids, APPLICATION_JSON));

			if (response.getStatusInfo().getFamily() == SUCCESSFUL)
			{
				return response.readEntity(new GenericType<Set<TaskBean>>() {});
			}
			else
					// handle business error (payload known)
					throw new NonTechnicalException(response.readEntity(ErrorResponse.class));
		}
		catch (ProcessingException e)
		{
			// this is thrown for technical issues (server down, wrong URL, timeout)
			throw new TechnicalException("communication error", e);
		}
	}

	@Override public Set<TaskLazy> findTasksLazy(@NonNull Set<Long> ids) throws TechnicalException, NonTechnicalException
	{
		try
		{
			WebTarget target   = client.target(webTarget.getUri() + Paths.BY_IDS_WITH_RELATED_LAZY);
			Response  response = target.request(APPLICATION_JSON).post(entity(ids, APPLICATION_JSON));

			if (response.getStatusInfo().getFamily() == SUCCESSFUL)
			{
				return response.readEntity(new GenericType<Set<TaskLazy>>() {});
			}
			else
					// handle business error (payload known)
					throw new NonTechnicalException(response.readEntity(ErrorResponse.class));
		}
		catch (ProcessingException e)
		{
			// this is thrown for technical issues (server down, wrong URL, timeout)
			throw new TechnicalException("communication error", e);
		}
	}

	@Override public void addSubTask(@NonNull final TaskBean task, @NonNull final TaskBean subTask)
			throws TechnicalException, NonTechnicalException
	{
		try
		{
			WebTarget target   = client.target(webTarget.getUri() + Paths.ADD_SUB);
			Response  response =
					target
							.request(APPLICATION_JSON)
							.put(entity(new InterTaskRelationData(task.id(), subTask.id()), APPLICATION_JSON));

			throwExceptionForNoSuccessInRelationalOperationResponse(response);
			// fire event to indicate that a new relation for the task with subtask has been created in the backend
			CDIUtil.fire(new AddNewPredecessorRelationInBackendEvent(this, task, subTask));
		}
		catch (ProcessingException e)
		{
			// this is thrown for technical issues (server down, wrong URL, timeout)
			throw new TechnicalException("communication error", e);
		}
	}

	@Override public void addPredecessor(@NonNull final TaskBean task, @NonNull final TaskBean predecessor)
			throws TechnicalException, NonTechnicalException
	{
		try
		{
			WebTarget target   = client.target(webTarget.getUri() + Paths.ADD_PREDECESSOR);
			Response  response =
					target
							.request(APPLICATION_JSON)
							.put(entity(new InterTaskRelationData(task.id(), predecessor.id()), APPLICATION_JSON));

			throwExceptionForNoSuccessInRelationalOperationResponse(response);
			// fire event to indicate that a new relation for the task with predecessor has been created in the backend
			CDIUtil.fire(new AddNewPredecessorRelationInBackendEvent(this, task, predecessor));
		}
		catch (ProcessingException e)
		{
			// this is thrown for technical issues (server down, wrong URL, timeout)
			throw new TechnicalException("communication error", e);
		}
	}

	@Override public void addSuccessor(@NonNull final TaskBean task, @NonNull final TaskBean successor)
			throws TechnicalException, NonTechnicalException
	{
		try
		{
			WebTarget target   = client.target(webTarget.getUri() + Paths.ADD_SUCCESSOR);
			Response  response =
					target
							.request(APPLICATION_JSON)
							.put(entity(new InterTaskRelationData(task.id(), successor.id()), APPLICATION_JSON));

			throwExceptionForNoSuccessInRelationalOperationResponse(response);
			// fire event to indicate that a new relation for the task with successor has been created in the backend
			CDIUtil.fire(new AddNewSuccessorRelationInBackendEvent(this, task, successor));
		}
		catch (ProcessingException e)
		{
			// this is thrown for technical issues (server down, wrong URL, timeout)
			throw new TechnicalException("communication error", e);
		}
	}

	@Override public void removeSuperSubTask(@NonNull final TaskBean task, @NonNull final TaskBean subTask)
			throws TechnicalException, NonTechnicalException
	{
		try
		{
			WebTarget target   = client.target(webTarget.getUri() + Paths.REMOVE_SUB);
			Response  response =
					target
							.request(APPLICATION_JSON)
							.put(entity(new InterTaskRelationData(task.id(), subTask.id()), APPLICATION_JSON));

			throwExceptionForNoSuccessInRelationalOperationResponse(response);
			// fire event to indicate that the relation for the task with subtask has been removed in the backend
			CDIUtil.fire(new RemoveSubTaskRelationInBackendEvent(this, task, subTask));
		}
		catch (ProcessingException e)
		{
			// this is thrown for technical issues (server down, wrong URL, timeout)
			throw new TechnicalException("communication error", e);
		}
	}

	@Override public void removePredecessor(@NonNull final TaskBean task, @NonNull final TaskBean predecessor)
			throws TechnicalException, NonTechnicalException
	{
		try
		{
			WebTarget target   = client.target(webTarget.getUri() + Paths.REMOVE_PREDECESSOR);
			Response  response =
					target
							.request(APPLICATION_JSON)
							.put(entity(new InterTaskRelationData(task.id(), predecessor.id()), APPLICATION_JSON));

			throwExceptionForNoSuccessInRelationalOperationResponse(response);
			// fire event to indicate that the relation for the task with subtask has been removed in the backend
			CDIUtil.fire(new RemovePredecessorRelationInBackendEvent(this, task, predecessor));
		}
		catch (ProcessingException e)
		{
			// this is thrown for technical issues (server down, wrong URL, timeout)
			throw new TechnicalException("communication error", e);
		}
	}

	@Override public void removeSuccessor(@NonNull final TaskBean task, @NonNull final TaskBean successor)
			throws TechnicalException, NonTechnicalException
	{
		try
		{
			WebTarget target   = client.target(webTarget.getUri() + Paths.REMOVE_SUCCESSOR);
			Response  response =
					target
							.request(APPLICATION_JSON)
							.put(entity(new InterTaskRelationData(task.id(), successor.id()), APPLICATION_JSON));

			throwExceptionForNoSuccessInRelationalOperationResponse(response);
			// fire event to indicate that the relation for the task with subtask has been removed in the backend
			CDIUtil.fire(new RemoveSuccessorRelationInBackendEvent(this, task, successor));
		}
		catch (ProcessingException e)
		{
			// this is thrown for technical issues (server down, wrong URL, timeout)
			throw new TechnicalException("communication error", e);
		}
	}

	@Override public void removeNeighboursFromTask(
			@NonNull RemoveNeighboursFromTaskConfig removeNeighboursFromTaskConfig)
			throws TechnicalException, NonTechnicalException
	{
		try
		{
			WebTarget target   = client.target(webTarget.getUri() + Paths.REMOVE_NEIGHBOURS_FROM_TASK);
			Response  response =
					target
							.request(APPLICATION_JSON)
							.put(entity(removeNeighboursFromTaskConfig, APPLICATION_JSON));

			try
			{
				throwExceptionForNoSuccessInRelationalOperationResponse(response);
			}
			catch (TaskRelationException e) { throw e; }
		}
		catch (ProcessingException e)
		{
			// this is thrown for technical issues (server down, wrong URL, timeout)
			throw new TechnicalException("communication error", e);
		}
	}

	private void throwExceptionForNoSuccessInRelationalOperationResponse(Response response)
			throws TechnicalException, NonTechnicalException
	{
		if (response.getStatusInfo().getFamily() == SUCCESSFUL) return;

		if (response.getStatus() == Status.CONFLICT.getStatusCode())
		{
			// try to read the error message from the response
			// create a new error message if the response is not a valid JSON or does not contain a message
			NonTechnicalException error = null;
			try
			{
				error = response.readEntity(NonTechnicalException.class);
				if (isNull(error.getMessage()))
						throw new NonTechnicalException(new ErrorResponse("invalid task relation", "unknown cause"));
				else
						throw error;
			}
			catch (ProcessingException e)
			{
				// this is thrown for technical issues (server down, wrong URL, timeout)
				throw new TechnicalException("communication error", e);
			}
		}
	}

	public static class TaskCreatedInBackendEvent extends AbstractEvent<TaskServiceClient, TaskBean>
	{
		public TaskCreatedInBackendEvent(@NonNull TaskServiceClient source, @NonNull TaskBean task) { super(source, task); }

		@ApplicationScoped
		public static class TaskCreatedInBackendEventDispatcher extends EventDispatcher<TaskCreatedInBackendEvent> { }
	}

	public static class TaskUpdatedInBackendEvent extends AbstractEvent<TaskServiceClient, TaskBean>
	{
		public TaskUpdatedInBackendEvent(@NonNull TaskServiceClient source, @NonNull TaskBean task) { super(source, task); }

		@ApplicationScoped
		public static class TaskUpdatedInBackendEventDispatcher extends EventDispatcher<TaskUpdatedInBackendEvent> { }
	}

	public static class TaskDeletedInBackendEvent extends AbstractEvent<TaskServiceClient, Long>
	{
		public TaskDeletedInBackendEvent(@NonNull TaskServiceClient source, @NonNull Long id) { super(source, id); }

		@ApplicationScoped
		public static class TaskDeletedInBackendEventDispatcher extends EventDispatcher<TaskDeletedInBackendEvent> { }
	}

	public static class AddNewSubTaskRelationInBackendEvent extends AbstractEvent<TaskServiceClient, TaskRelationInfo>
	{
		public AddNewSubTaskRelationInBackendEvent(TaskServiceClient taskServiceClient, @NonNull TaskBean task, @NonNull TaskBean subTask)
		{
			super(taskServiceClient, new TaskRelationInfo(task, subTask));
		}

		@ApplicationScoped
		public static class AddNewSubTaskRelationInBackendEventDispatcher extends EventDispatcher<TaskRelationInfo> { }
	}

	public static class AddNewPredecessorRelationInBackendEvent extends AbstractEvent<TaskServiceClient, TaskRelationInfo>
	{
		public AddNewPredecessorRelationInBackendEvent(TaskServiceClient taskServiceClient, @NonNull TaskBean task, @NonNull TaskBean predecessor)
		{
			super(taskServiceClient, new TaskRelationInfo(task, predecessor));
		}

		@ApplicationScoped
		public static class AddNewPredecessorRelationInBackendEventDispatcher extends EventDispatcher<TaskRelationInfo> { }
	}

	public static class AddNewSuccessorRelationInBackendEvent extends AbstractEvent<TaskServiceClient, TaskRelationInfo>
	{
		public AddNewSuccessorRelationInBackendEvent(TaskServiceClient taskServiceClient, @NonNull TaskBean task, @NonNull TaskBean successor)
		{
			super(taskServiceClient, new TaskRelationInfo(task, successor));
		}

		@ApplicationScoped
		public static class AddNewSuccessorRelationInBackendEventDispatcher extends EventDispatcher<TaskRelationInfo> { }
	}

	public static class RemoveSubTaskRelationInBackendEvent extends AbstractEvent<TaskServiceClient, TaskRelationInfo>
	{
		public RemoveSubTaskRelationInBackendEvent(TaskServiceClient taskServiceClient, @NonNull TaskBean task, @NonNull TaskBean subTask)
		{
			super(taskServiceClient, new TaskRelationInfo(task, subTask));
		}

		@ApplicationScoped
		public static class RemoveSubTaskRelationInBackendEventDispatcher extends EventDispatcher<TaskRelationInfo> { }
	}

	public static class RemovePredecessorRelationInBackendEvent extends AbstractEvent<TaskServiceClient, TaskRelationInfo>
	{
		public RemovePredecessorRelationInBackendEvent(TaskServiceClient taskServiceClient, @NonNull TaskBean task, @NonNull TaskBean subTask)
		{
			super(taskServiceClient, new TaskRelationInfo(task, subTask));
		}

		@ApplicationScoped
		public static class RemovePredecessorRelationInBackendEventDispatcher extends EventDispatcher<TaskRelationInfo> { }
	}

	public static class RemoveSuccessorRelationInBackendEvent extends AbstractEvent<TaskServiceClient, TaskRelationInfo>
	{
		public RemoveSuccessorRelationInBackendEvent(TaskServiceClient taskServiceClient, @NonNull TaskBean task, @NonNull TaskBean subTask)
		{
			super(taskServiceClient, new TaskRelationInfo(task, subTask));
		}

		@ApplicationScoped
		public static class RemoveSuccessorRelationInBackendEventDispatcher extends EventDispatcher<TaskRelationInfo> { }
	}

	@Accessors(fluent = true)
	public record TaskRelationInfo(@NonNull TaskBean task, @NonNull TaskBean relatedTask) { }
}