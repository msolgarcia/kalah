package com.kalah.web.controller;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class NewMatch {
	
	@NotBlank
	@Size(max = 100)
	private String playerOneName;
	
	@NotBlank
	@Size(max = 100)
	private String playerTwoName;

	public String getPlayerOneName() {
		return playerOneName;
	}

	public void setPlayerOneName(String playerOneName) {
		this.playerOneName = playerOneName;
	}

	public String getPlayerTwoName() {
		return playerTwoName;
	}

	public void setPlayerTwoName(String playerTwoName) {
		this.playerTwoName = playerTwoName;
	}
	
	

}
