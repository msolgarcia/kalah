package com.kalah.web.controller;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kalah.model.Match;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MatchResponse {
	
	private int id;
	
	private String namePlayerOne;
	
	private String namePlayerTwo;
	
	private int nextTurnPlayerId;
	
	private boolean finishedMatch = false;
	
	private Integer winnerPlayerId;
	
	private Map<Integer, Integer> playerOnePits;
	
	private int playerOneKalah;
	
	private Map<Integer, Integer> playerTwoPits;
	
	private int playerTwoKalah;
	
	public MatchResponse(Match match) {
		id = match.getId();
		namePlayerOne = match.getPlayerOne();
		namePlayerTwo = match.getPlayerTwo();
		nextTurnPlayerId = match.getTurnPlayerId();
		finishedMatch = match.getWinner() != null ? true : false;
		winnerPlayerId = match.getWinner();
		playerOnePits = match.getBoard().getPits().get(1).getPits();
		playerTwoPits = match.getBoard().getPits().get(2).getPits();
		playerOneKalah = match.getBoard().getKalahs().get(1);
		playerTwoKalah = match.getBoard().getKalahs().get(2);
	}

	public int getId() {
		return id;
	}

	public String getNamePlayerOne() {
		return namePlayerOne;
	}

	public String getNamePlayerTwo() {
		return namePlayerTwo;
	}

	public int getNextTurnPlayerId() {
		return nextTurnPlayerId;
	}

	public boolean isFinishedMatch() {
		return finishedMatch;
	}

	public Integer getWinnerPlayerId() {
		return winnerPlayerId;
	}

	public Map<Integer, Integer> getPlayerOnePits() {
		return playerOnePits;
	}

	public int getPlayerOneKalah() {
		return playerOneKalah;
	}

	public Map<Integer, Integer> getPlayerTwoPits() {
		return playerTwoPits;
	}

	public int getPlayerTwoKalah() {
		return playerTwoKalah;
	}
	
}
