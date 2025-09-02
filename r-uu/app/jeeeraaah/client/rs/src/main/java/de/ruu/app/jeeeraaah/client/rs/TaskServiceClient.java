package de.ruu.app.jeeeraaah.client.rs;

import de.ruu.app.jeeeraaah.common.InterTaskRelationData;
import de.ruu.app.jeeeraaah.common.Paths;
import de.ruu.app.jeeeraaah.common.RemoveNeighboursFromTaskConfig;
import de.ruu.app.jeeeraaah.common.TaskCreationData;
import de.ruu.app.jeeeraaah.common.TaskRelationException;
import de.ruu.app.jeeeraaah.common.TaskService;
import de.ruu.app.jeeeraaah.common.TaskServiceLazy;
import de.ruu.app.jeeeraaah.common.dto.TaskEntityDTO;
import de.ruu.app.jeeeraaah.common.dto.TaskLazy;
import de.ruu.lib.jackson.JacksonContextResolver;
import de.ruu.lib.util.rs.ExceptionDTO;
import de.ruu.lib.util.rs.RestClientCallException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Singleton;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.ConfigProvider;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static de.ruu.lib.util.BooleanFunctions.not;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.CONFLICT;
import static jakarta.ws.rs.core.Response.Status.OK;
import static java.util.Objects.isNull;

@Singleton
@Slf4j
public class TaskServiceClient
		implements TaskService<TaskEntityDTO>, TaskServiceLazy
{
	private static final String UNEXPECTED_STATUS = "unexpected status: ";

	private String  scheme =
			ConfigProvider.getConfig().getOptionalValue("jeeeraaah.rest-api.scheme", String.class).orElse("http");

	private String  host   =
			ConfigProvider.getConfig().getOptionalValue("jeeeraaah.rest-api.host"  , String.class).orElse("127.0.0.1");

	private Integer port   =
			ConfigProvider.getConfig().getOptionalValue("jeeeraaah.rest-api.port"  , Integer.class).orElse(9080);

	private URI uri;

	private Client client;

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// lifecycle methods
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@PostConstruct public void postConstruct()
	{
		String schemaHostPort = scheme + "://" + host + ":" + port;

		uri = URI.create(schemaHostPort + Paths.PATH_TO_APP + Paths.PATH_APPENDER_TO_DOMAIN_TASK);

		log.debug("scheme        : {}", scheme);
		log.debug("host          : {}", host);
		log.debug("port          : {}", port);
		log.debug("schemaHostPort: {}", schemaHostPort);
		log.debug("uri           : {}", uri);

		client = ClientBuilder.newClient();
//		client.register(JacksonJsonProvider.class);
//log.debug("registered {}", JacksonJsonProvider.class);
		client.register(JacksonContextResolver.class);
log.debug("registered {}", JacksonContextResolver.class);
//		client.register(new ClientLoggingFilter());

		List<String> classNames = new ArrayList<>();
		client.getConfiguration().getClasses().forEach(c -> classNames.add(c.getName()));
		log.debug("rs client configuration classes\n{}", String.join("\n", classNames));
	}

	@PreDestroy public void preDestroy() { client.close(); }

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// interface implementations
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override public @NonNull TaskEntityDTO create(@NonNull TaskEntityDTO task)
	{
		Response response =
				client
						.target(uri)
						.request()
						.post(Entity.entity(new TaskCreationData(task.taskGroup().id(), task), APPLICATION_JSON));

		if (not(response.getStatus() == Status.CREATED.getStatusCode()))
		{
			throw new RestClientCallException(UNEXPECTED_STATUS + response.getStatus() + "\nuri: " + uri, response);
		}

		return response.readEntity(TaskEntityDTO.class);
	}

	@Override public Optional<? extends TaskEntityDTO> read(@NonNull Long id)
	{
		WebTarget target   = client.target(uri + Paths.BY_ID);
		Response  response = target.resolveTemplate("id", id).request().get();

		int status = response.getStatus();

		if (status == OK.getStatusCode())
		{
			return Optional.of(response.readEntity(TaskEntityDTO.class));
		}
		else if (status == Status.NOT_FOUND.getStatusCode())
		{
			return Optional.empty();
		}
		else
		{
			throw new RestClientCallException(UNEXPECTED_STATUS + status + "\nuri: " + target.getUri(), response);
		}
	}

	@Override public @NonNull TaskEntityDTO update(@NonNull TaskEntityDTO task)
	{
		Response response = client.target(uri).request().put(Entity.entity(task, APPLICATION_JSON));

		if (not(response.getStatus() == OK.getStatusCode()))
		{
			throw new RestClientCallException(UNEXPECTED_STATUS + response.getStatus() + "\nuri: " + uri, response);
		}

		return response.readEntity(TaskEntityDTO.class);
	}

	@Override public void delete(@NonNull Long id)
	{
		WebTarget target   = client.target(uri + Paths.BY_ID);
		Response  response = target.resolveTemplate("id", id).request().delete();

		if (not(response.getStatus() == OK.getStatusCode()))
		{
			throw new RestClientCallException(UNEXPECTED_STATUS + response.getStatus() + "\nuri: " + target.getUri(), response);
		}
	}

	@Override public Set<TaskEntityDTO> findAll()
	{
		Response response = client.target(uri).request().get();

		if (not(response.getStatus() == OK.getStatusCode()))
		{
			throw new RestClientCallException(UNEXPECTED_STATUS + response.getStatus() + "\nuri: " + uri, response);
		}

		return response.readEntity(new GenericType<HashSet<TaskEntityDTO>>() { });
	}

	@Override public Optional<TaskEntityDTO> findWithRelated(@NonNull Long id)
	{
		WebTarget target   = client.target(uri + Paths.BY_ID_WITH_RELATED);
		Response  response = target.resolveTemplate("id", id).request().get();

		if (not(response.getStatus() == OK.getStatusCode()))
		{
			throw new RestClientCallException(UNEXPECTED_STATUS + response.getStatus() + "\nuri: " + target.getUri(), response);
		}

		TaskEntityDTO result = response.readEntity(TaskEntityDTO.class);

		return Optional.of(result);
	}

	@Override public Set<TaskLazy> findTasksLazy(@NonNull Set<Long> ids)
	{
		WebTarget target   = client.target(uri + Paths.BY_IDS_WITH_RELATED_LAZY);
		Response  response = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(ids, MediaType.APPLICATION_JSON));

		if (not(response.getStatus() == OK.getStatusCode()))
		{
			throw new RestClientCallException(UNEXPECTED_STATUS + response.getStatus() + "\nuri: " + target.getUri(), response);
		}

		return response.readEntity(new GenericType<Set<TaskLazy>>() {});
	}

	@Override public void addSubTask(@NonNull TaskEntityDTO task, @NonNull TaskEntityDTO subTask)
			throws TaskRelationException, NotFoundException
	{
		WebTarget target   = client.target(uri + Paths.ADD_SUB);
		Response  response =
				target
						.request(APPLICATION_JSON)
						.put(Entity.entity(new InterTaskRelationData(task.id(), subTask.id()), APPLICATION_JSON));

		handleTaskRelationOperationResponse(response, target);
	}

	@Override public void addPredecessor(@NonNull TaskEntityDTO task, @NonNull TaskEntityDTO predecessor)
			throws TaskRelationException, NotFoundException
	{
		WebTarget target   = client.target(uri + Paths.ADD_PREDECESSOR);
		Response  response =
				target
						.request(APPLICATION_JSON)
						.put(Entity.entity(new InterTaskRelationData(task.id(), predecessor.id()), APPLICATION_JSON));

		handleTaskRelationOperationResponse(response, target);
	}

	@Override public void addSuccessor(@NonNull TaskEntityDTO task, @NonNull TaskEntityDTO successor)
			throws TaskRelationException, NotFoundException
	{
		WebTarget target   = client.target(uri + Paths.ADD_SUCCESSOR);
		Response  response =
				target
						.request(APPLICATION_JSON)
						.put(Entity.entity(new InterTaskRelationData(task.id(), successor.id()), APPLICATION_JSON));

		handleTaskRelationOperationResponse(response, target);
	}

	@Override public void removeSuperSubTask(@NonNull TaskEntityDTO task, @NonNull TaskEntityDTO subTask)
			throws TaskRelationException, NotFoundException
	{
		WebTarget target   = client.target(uri + Paths.REMOVE_SUB);
		Response  response =
				target
						.request(APPLICATION_JSON)
						.put(Entity.entity(new InterTaskRelationData(task.id(), subTask.id()), APPLICATION_JSON));

		handleTaskRelationOperationResponse(response, target);
	}

	@Override public void removePredecessor(@NonNull TaskEntityDTO task, @NonNull TaskEntityDTO predecessor)
			throws TaskRelationException, NotFoundException
	{
		WebTarget target   = client.target(uri + Paths.REMOVE_PREDECESSOR);
		Response  response =
				target
						.request(APPLICATION_JSON)
						.put(Entity.entity(new InterTaskRelationData(task.id(), predecessor.id()), APPLICATION_JSON));

		handleTaskRelationOperationResponse(response, target);
	}

	@Override public void removeSuccessor(@NonNull TaskEntityDTO task, @NonNull TaskEntityDTO successor)
			throws TaskRelationException, NotFoundException
	{
		WebTarget target   = client.target(uri + Paths.REMOVE_SUCCESSOR);
		Response  response =
				target
						.request(APPLICATION_JSON)
						.put(Entity.entity(new InterTaskRelationData(task.id(), successor.id()), APPLICATION_JSON));

		handleTaskRelationOperationResponse(response, target);
	}

	@Override public boolean removeNeighboursFromTask(
			@NonNull RemoveNeighboursFromTaskConfig removeNeighboursFromTaskConfig)
			throws TaskRelationException, NotFoundException
	{
		WebTarget target   = client.target(uri + Paths.REMOVE_NEIGHBOURS_FROM_TASK);
		Response  response =
				target
						.request(APPLICATION_JSON)
						.put(Entity.entity(removeNeighboursFromTaskConfig, APPLICATION_JSON));

		try
		{
			handleTaskRelationOperationResponse(response, target);
			return true;
		}
		catch (TaskRelationException e) { throw e; }
	}

	private void handleTaskRelationOperationResponse(Response response, WebTarget target)
			throws TaskRelationException
	{
		int status =  response.getStatus();
		if (status == OK      .getStatusCode()) return;
		if (status == CONFLICT.getStatusCode())
		{
			// try to read the error message from the response
			// create a new error message if the response is not a valid JSON or does not contain a message
			ExceptionDTO error = null;
			try
			{
				error = response.readEntity(ExceptionDTO.class);
				if (isNull(error) || isNull(error.message())) error = new ExceptionDTO("invalid task relation", "");
			}
			catch (Exception ignored)
			{
				error = new ExceptionDTO("invalid task relation", "");
			}
			throw new TaskRelationException(error.message());
		}

		throw new RestClientCallException(UNEXPECTED_STATUS + response.getStatus() + "\nuri: " + target.getUri(), response);
	}
}