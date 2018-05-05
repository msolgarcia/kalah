package com.kalah.web.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalKalahException extends RuntimeException {

	public InternalKalahException() {
		super("Internal Server Error");
	}
}
