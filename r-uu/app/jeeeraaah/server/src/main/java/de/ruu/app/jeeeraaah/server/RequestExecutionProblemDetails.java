package de.ruu.app.jeeeraaah.server;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter @Accessors(fluent = true)
public class RequestExecutionProblemDetails
{
	private String type;
	private String title;
	private int    status;
	private String detail;

	public RequestExecutionProblemDetails() {} // for JSON-B/Jackson

	public RequestExecutionProblemDetails(String type, String title, int status, String detail)
	{
		this.type   = type;
		this.title  = title;
		this.status = status;
		this.detail = detail;
	}
}