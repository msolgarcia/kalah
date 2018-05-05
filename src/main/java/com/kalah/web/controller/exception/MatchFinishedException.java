package com.kalah.web.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MatchFinishedException extends RuntimeException {

	public MatchFinishedException(Integer matchId) {
		super(String.format("Error trying to play in a finished match id: %d", matchId));
	}
}
