package de.ruu.lib.ws.rs;

import lombok.Getter;

public class NonTechnicalException extends Exception {
	@Getter
	private final ErrorResponse errorResponse;

	public NonTechnicalException(ErrorResponse errorResponse) {
		super(errorResponse.message());
		this.errorResponse = errorResponse;
	}
}
