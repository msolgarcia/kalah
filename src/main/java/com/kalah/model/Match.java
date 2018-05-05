package com.kalah.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Match {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(nullable = false)
	private String playerOne;
	
	@Column(nullable = false)
	private String playerTwo;
	
	@Column(nullable = false)
	private Integer turnPlayerId;
	
	@Column(nullable = true)
	private Integer winner;
	
	@OneToOne(optional = false, cascade = CascadeType.ALL)
	private Board board;
	
	protected Match() {}
	
	public Match(String playerOneName, String playerTwoName) {
		playerOne = playerOneName;
		playerTwo = playerTwoName;
		turnPlayerId = Integer.valueOf(1);
		board = new Board();
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPlayerOne() {
		return playerOne;
	}

	public void setPlayerOne(String playerOne) {
		this.playerOne = playerOne;
	}

	public String getPlayerTwo() {
		return playerTwo;
	}

	public void setPlayerTwo(String playerTwo) {
		this.playerTwo = playerTwo;
	}
	
	public Integer getTurnPlayerId() {
		return turnPlayerId;
	}

	public void setTurnPlayerId(Integer turnPlayerId) {
		this.turnPlayerId = turnPlayerId;
	}

	public Integer getWinner() {
		return winner;
	}

	public void setWinner(Integer winner) {
		this.winner = winner;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public boolean hasStones(Integer player, Integer pit) {
		if(board != null) {
			return board.getStones(player, pit) > 0 ? true : false;
		}
		return false;
	}
	
	public void sowsStones(int player, int pit) {
		
		boolean additionalTurn = board.sowStones(player, pit);
		
		if(board.hasEmptyPits()) {
			board.moveStonesToKalah();
			winner = board.getWinner();
		} else {
			if(!additionalTurn) {
				int rival = player == 1 ? 2 : 1;
				turnPlayerId = rival;
			}
		}
	}

	@Override
	public String toString() {
		return "Board [" + board + "]"
				+ "\nWinner: " + winner
				+ "\nNextTurn: " + turnPlayerId;
	}

}
