package de.ruu.app.jeeeraaah.frontend.ws.rs;

import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import de.ruu.app.jeeeraaah.common.api.bean.TaskGroupBean;
import de.ruu.app.jeeeraaah.common.api.domain.Paths;
import de.ruu.app.jeeeraaah.common.api.domain.TaskGroupFlat;
import de.ruu.app.jeeeraaah.common.api.domain.TaskGroupLazy;
import de.ruu.app.jeeeraaah.common.api.domain.TaskGroupService;
import de.ruu.app.jeeeraaah.common.api.domain.TaskGroupServiceLazy;
import de.ruu.app.jeeeraaah.common.api.mapping.Mappings;
import de.ruu.app.jeeeraaah.common.api.ws.rs.TaskGroupDTO;
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
import static jakarta.ws.rs.client.Entity.entity;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;
import static jakarta.ws.rs.core.Response.Status.CONFLICT;
import static jakarta.ws.rs.core.Response.Status.Family.SUCCESSFUL;

@Singleton
@Slf4j
public class TaskGroupServiceClient
		implements TaskGroupService<TaskGroupBean>, TaskGroupServiceLazy
{
	private static final String UNEXPECTED_STATUS = "unexpected status: ";

	private final String scheme =
			ConfigProvider.getConfig().getOptionalValue("jeeeraaah.rest-api.scheme", String.class).orElse("http");

	private final String host =
			ConfigProvider.getConfig().getOptionalValue("jeeeraaah.rest-api.host"  , String.class).orElse("127.0.0.1");

	private final Integer port =
			ConfigProvider.getConfig().getOptionalValue("jeeeraaah.rest-api.port"  , Integer.class).orElse(9080);

	private Client client;

	private WebTarget webTarget;

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// lifecycle methods
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@PostConstruct 
	public void postConstruct()
	{
		String schemaHostPort = scheme + "://" + host + ":" + port;

		URI uri = URI.create(schemaHostPort + Paths.PATH_TO_APP + Paths.PATH_APPENDER_TO_DOMAIN_TASK_GROUP);

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

	@Override public @NonNull TaskGroupBean create(@NonNull TaskGroupBean taskGroup) throws TechnicalException, NonTechnicalException
	{
		try
		{
			Response response = webTarget.request().post(entity(toDTO(taskGroup, newContext()), APPLICATION_JSON));

			if (response.getStatusInfo().getFamily() == SUCCESSFUL)
			{
				TaskGroupDTO  dto    = response.readEntity(TaskGroupDTO.class);
				TaskGroupBean result = toBean(dto,newContext());
				// fire event to indicate that a new task group has been created in the backend
				CDIUtil.fire(new TaskGroupCreatedInBackendEvent(this, result));
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

	@Override public Optional<TaskGroupBean> read(@NonNull Long id) throws TechnicalException, NonTechnicalException
	{
		try
		{
			Response response = webTarget.resolveTemplate("id", id).request().get();
			int status = response.getStatus();

			if (status == Status.OK.getStatusCode())
			{
				TaskGroupDTO dto = response.readEntity(TaskGroupDTO.class);
				return Optional.of(Mappings.toBean(dto, newContext()));
			}
			else if (status == Status.NOT_FOUND.getStatusCode())
			{
				return Optional.empty();
			}
			else
			{
				// handle business error (payload known)
				throw new NonTechnicalException(response.readEntity(ErrorResponse.class));
			}
		}
		catch (ProcessingException e)
		{
			// this is thrown for technical issues (server down, wrong URL, timeout)
			throw new TechnicalException("communication error", e);
		}
	}

	@Override public @NonNull TaskGroupBean update(@NonNull TaskGroupBean taskGroup) throws TechnicalException, NonTechnicalException
	{
		try
		{
			Response response = webTarget.request().put(entity(toDTO(taskGroup, newContext()), APPLICATION_JSON));

			if (response.getStatusInfo().getFamily() == SUCCESSFUL)
			{
				TaskGroupDTO  dto = response.readEntity(TaskGroupDTO.class);
				TaskGroupBean result = toBean(dto, newContext());
				// fire event to indicate that a task group has been updated in the backend
				CDIUtil.fire(new TaskGroupUpdatedInBackendEvent(this, result));
				return result;
			}
			else
			{
				// handle business error (payload known)
				throw new NonTechnicalException(response.readEntity(ErrorResponse.class));
			}
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
				// fire event to indicate that a task group has been deleted in the backend
				CDIUtil.fire(new TaskGroupDeletedInBackendEvent(this, id));
			}
			else if (response.getStatus() != Status.NOT_FOUND.getStatusCode())
			{
				// Only throw an exception if it's not a 404 (Not Found)
				throw new NonTechnicalException(response.readEntity(ErrorResponse.class));
			}
		}
		catch (ProcessingException e)
		{
			// this is thrown for technical issues (server down, wrong URL, timeout)
			throw new TechnicalException("communication error", e);
		}
	}

	@Override public Set<TaskGroupBean> findAll() throws TechnicalException, NonTechnicalException
	{
		try
		{
			Response response = webTarget.request().get();

			if (response.getStatusInfo().getFamily() == SUCCESSFUL)
			{
				return
						response
								.readEntity(new GenericType<HashSet<TaskGroupDTO>>() { })
								.stream()
								.map(dto -> toBean(dto, newContext()))
								.collect(Collectors.toSet());
			}
			else
			{
				// handle business error (payload known)
				throw new NonTechnicalException(response.readEntity(ErrorResponse.class));
			}
		}
		catch (ProcessingException e)
		{
			// this is thrown for technical issues (server down, wrong URL, timeout)
			throw new TechnicalException("communication error", e);
		}
	}

	@Override public Optional<TaskGroupBean> findWithTasks(@NonNull Long id) throws TechnicalException, NonTechnicalException
	{
		try
		{
			WebTarget target   = client.target(webTarget.getUri() + Paths.BY_ID_WITH_TASKS);
			Response  response = target.resolveTemplate("id", id).request().get();

			if (response.getStatusInfo().getFamily() == SUCCESSFUL)
			{
				TaskGroupDTO dto = response.readEntity(TaskGroupDTO.class);
				return Optional.of(toBean(dto, newContext()));
			}
			else if (response.getStatus() == Status.NOT_FOUND.getStatusCode())
			{
				return Optional.empty();
			}
			else
			{
				// handle business error (payload known)
				throw new NonTechnicalException(response.readEntity(ErrorResponse.class));
			}
		}
		catch (ProcessingException e)
		{
			// this is thrown for technical issues (server down, wrong URL, timeout)
			throw new TechnicalException("communication error", e);
		}
	}

	@Override public Optional<TaskGroupLazy> findGroupLazy(@NonNull Long id) throws TechnicalException, NonTechnicalException
	{
		try
		{
			WebTarget target   = client.target(webTarget.getUri() + Paths.BY_ID_LAZY);
			Response  response = target.resolveTemplate("id", id).request().get();

			if (response.getStatusInfo().getFamily() == SUCCESSFUL)
			{
				return Optional.of(response.readEntity(TaskGroupLazy.class));
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

	@Override public Set<TaskGroupLazy> findAllLazy() throws TechnicalException, NonTechnicalException
	{
		try
		{
			WebTarget target   = client.target(webTarget.getUri() + Paths.ALL_FLAT);
			Response  response = target.request(APPLICATION_JSON).get();

			if (response.getStatusInfo().getFamily() == SUCCESSFUL)
			{
				return response.readEntity(new GenericType<Set<TaskGroupLazy>>() {});
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

	@Override public Optional<TaskGroupFlat> findGroupFlat(@NonNull Long id) throws TechnicalException, NonTechnicalException
	{
		try
		{
			WebTarget target   = client.target(webTarget.getUri() + Paths.BY_ID_LAZY);
			Response  response = target.resolveTemplate("id", id).request().get();

			if (response.getStatusInfo().getFamily() == SUCCESSFUL)
			{
				return Optional.of(response.readEntity(TaskGroupFlat.class));
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

	// TODO: think about using a single task entity parameter instead of two Longs
	@Override public void removeFromGroup(@NonNull Long idGroup, @NonNull Long idTask) throws TechnicalException, NonTechnicalException
	{
		try
		{
			WebTarget target   = client.target(webTarget.getUri() + Paths.REMOVE_TASK_FROM_GROUP);
			Response  response =
					target.resolveTemplate("idGroup", idGroup).resolveTemplate("idTask", idTask).request().get();

			throwExceptionForNoSuccessInRelationalOperationResponse(response);
			// fire event to indicate that the task has been removed from the group in the backend
			CDIUtil.fire(new RemoveFromGroupInBackendEvent(this, new TaskGroupTaskRelationInfo(idGroup, idTask)));
		}
		catch (ProcessingException e)
		{
			// this is thrown for technical issues (server down, wrong URL, timeout)
			throw new TechnicalException("communication error", e);
		}
	}

	public Set<TaskGroupFlat> findAllFlat() throws TechnicalException, NonTechnicalException
	{
		try
		{
			WebTarget target   = client.target(webTarget.getUri() + Paths.ALL_LAZY);
			Response  response = target.request(APPLICATION_JSON).get();

			if (response.getStatusInfo().getFamily() == SUCCESSFUL)
			{
				return response.readEntity(new GenericType<Set<TaskGroupFlat>>() {});
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

	private void throwExceptionForNoSuccessInRelationalOperationResponse(Response response)
			throws TechnicalException, NonTechnicalException
	{
		if (response.getStatusInfo().getFamily() == SUCCESSFUL) return;

		if (   response.getStatus() == CONFLICT.getStatusCode()
				|| response.getStatus() == BAD_REQUEST.getStatusCode())
		{
			try
			{
				ErrorResponse error = response.readEntity(ErrorResponse.class);
				throw new NonTechnicalException(error);
			}
			catch (ProcessingException e)
			{
				// fallback if we can't parse the error response
				throw new NonTechnicalException(new ErrorResponse("operation failed", response.getStatusInfo().getReasonPhrase()));
			}
		}
		else
		{
			// TODO: does it make sense to create a new Exception without any message here?
			throw new TechnicalException("unexpected response status: " + response.getStatus(), new Exception());
		}
	}

	public static class TaskGroupCreatedInBackendEvent extends AbstractEvent<TaskGroupServiceClient, TaskGroupBean>
	{
		public TaskGroupCreatedInBackendEvent(TaskGroupServiceClient taskGroupServiceClient, @NonNull TaskGroupBean taskGroup)
		{
			super(taskGroupServiceClient, taskGroup);
		}

		@ApplicationScoped
		public static class RemoveSubTaskRelationInBackendEventDispatcher extends EventDispatcher<TaskGroupBean> { }
	}

	public static class TaskGroupDeletedInBackendEvent extends AbstractEvent<TaskGroupServiceClient, Long>
	{
		public TaskGroupDeletedInBackendEvent(TaskGroupServiceClient taskGroupServiceClient, @NonNull Long id)
		{
			super(taskGroupServiceClient, id);
		}

		@ApplicationScoped
		public static class TaskGroupDeletedInBackendEventDispatcher extends EventDispatcher<Long> { }
	}

	public static class TaskGroupUpdatedInBackendEvent extends AbstractEvent<TaskGroupServiceClient, TaskGroupBean>
	{
		public TaskGroupUpdatedInBackendEvent(TaskGroupServiceClient taskGroupServiceClient, @NonNull TaskGroupBean taskGroup)
		{
			super(taskGroupServiceClient, taskGroup);
		}

		@ApplicationScoped
		public static class RemoveSubTaskRelationInBackendEventDispatcher extends EventDispatcher<TaskGroupBean> { }
	}

	public static class RemoveFromGroupInBackendEvent extends AbstractEvent<TaskGroupServiceClient, TaskGroupTaskRelationInfo>
	{
		public RemoveFromGroupInBackendEvent(TaskGroupServiceClient taskGroupServiceClient, @NonNull TaskGroupTaskRelationInfo taskGroupTaskRelationInfo)
		{
			super(taskGroupServiceClient, taskGroupTaskRelationInfo);
		}

		@ApplicationScoped
		public static class RemoveSubTaskRelationInBackendEventDispatcher extends EventDispatcher<TaskGroupTaskRelationInfo> { }
	}

	@Accessors(fluent = true)
	public record TaskGroupTaskRelationInfo(@NonNull Long taskGroupId, @NonNull Long taskId) { }

	private @NonNull ReferenceCycleTracking newContext() { return new ReferenceCycleTracking(); }
}