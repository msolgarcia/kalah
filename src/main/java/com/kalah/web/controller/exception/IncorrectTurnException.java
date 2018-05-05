package com.kalah.web.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class IncorrectTurnException extends RuntimeException {

	public IncorrectTurnException(Integer matchId, Integer turn, Integer matchTurn) {
		super(String.format(
				"Error trying to play an incorrect turn for match id: %d with player id %d when it was player id %d turn",
				matchId, turn, matchTurn));
	}
}
