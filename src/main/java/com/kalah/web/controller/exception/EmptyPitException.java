package com.kalah.web.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EmptyPitException extends RuntimeException {

	public EmptyPitException(Integer matchId, Integer player, Integer pit) {
		super(String.format("Error trying to pick stones from an empty pit, pit id: %d of player id %d for the match id %d", pit,
				player, matchId));
	}
}
