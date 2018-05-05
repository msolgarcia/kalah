package com.kalah.web.controller;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class Play {
	
	@Min(value = 1, message = "Incorrect player identifier, valid player identifiers are 1 or 2")
	@Max(value = 2, message = "Incorrect player identifier, valid player identifiers are 1 or 2")
	@NotNull
	private Integer player;
	
	@Min(value = 0, message = "Incorrect pit identifier, valid pit identifiers are [1 - 6]")
	@Max(value = 5, message = "Incorrect pit identifier, valid pit identifiers are [1 - 6]")
	@NotNull
	private Integer pit;

	public Integer getPlayer() {
		return player;
	}

	public void setPlayer(Integer player) {
		this.player = player;
	}

	public Integer getPit() {
		return pit;
	}

	public void setPit(Integer pit) {
		this.pit = pit;
	}
}