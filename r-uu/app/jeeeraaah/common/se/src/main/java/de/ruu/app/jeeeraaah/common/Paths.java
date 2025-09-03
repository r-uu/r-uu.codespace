package de.ruu.app.jeeeraaah.common;

public interface Paths
{
	/** general purpose constants */
	String BY_ID = "/{id}";

	/** constants for application */
	String PATH_TO_APP                        = "/jeee-raaah";

	/** constants for domain taskgroup */
	String PATH_APPENDER_TO_DOMAIN_TASK_GROUP = "/taskgroup";
	/** constants for domain task */
	String PATH_APPENDER_TO_DOMAIN_TASK       = "/task";

	/** special purpose constants */
	String BY_ID_WITH_RELATED                 = BY_ID + "/withRelated";
	String BY_ID_WITH_RELATED_LAZY            = BY_ID + "/withRelatedLazy";
	String BY_IDS_WITH_RELATED_LAZY           =         "/withRelatedLazy";
	String BY_ID_WITH_TASKS                   = BY_ID + "/withTasks";
	String BY_ID_LAZY                         = BY_ID + "/lazy";
	String ALL_FLAT                           = "/allFlat";
	String ADD_SUB                            = "/addSubTask";
	String ADD_PREDECESSOR                    = "/addPredecessor";
	String ADD_SUCCESSOR                      = "/addSuccessor";
	String REMOVE_SUB                         = "/removeSubTask";
	String REMOVE_PREDECESSOR                 = "/removePredecessor";
	String REMOVE_SUCCESSOR                   = "/removeSuccessor";
	String REMOVE_NEIGHBOURS_FROM_TASK        = "/removeTaskFromNeighbours";
	String REMOVE_TASK_FROM_GROUP             = "/removeTaskFromGroup/{idGroup}" + PATH_APPENDER_TO_DOMAIN_TASK + "/{idTask}";

	/** constants for application */
	String PATH_APPENDER_REST_API            = "/rest-api";

	/** constants for domain taskgroup */
	String PATH_APPENDER_REST_API_TASK_GROUP = PATH_APPENDER_REST_API + "taskgroup";
}