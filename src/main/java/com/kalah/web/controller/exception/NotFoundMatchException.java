package com.kalah.web.controller.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundMatchException extends RuntimeException {

	public NotFoundMatchException(Integer matchId) {
		super(String.format("Error trying to play in unexisting match id: %d", matchId));
	}
}
