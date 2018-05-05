package com.kalah.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kalah.model.Match;
import com.kalah.repository.MatchRepository;
import com.kalah.web.controller.exception.EmptyPitException;
import com.kalah.web.controller.exception.IncorrectTurnException;
import com.kalah.web.controller.exception.MatchFinishedException;
import com.kalah.web.controller.exception.NotFoundMatchException;

@Service
public class MatchService {

	final static Logger logger = LoggerFactory.getLogger(MatchService.class);
	
	@Autowired
	private MatchRepository matchRepository;
	
	public List<Match> getMatches(){
		return matchRepository.findAll();
	}
	
	public Match createMatch(String playerOneName, String playerTwoName) {

		Match match = new Match(playerOneName, playerTwoName);

		logger.debug("Match created: {}", match);

		return matchRepository.save(match);
	}

	public Match play(Integer matchId, Integer player, Integer pit) {
		Match match =  matchRepository.findOne(matchId);
		validatePlay(match, matchId, player, pit);

		match.sowsStones(player, pit);

		logger.debug("Match after play fo player {} and pit {}: {}", player, pit, match);

		return matchRepository.save(match);
	}
	
	private void validatePlay(Match match, Integer matchId, Integer player, Integer pit) {

		validateExistingMatch(match, matchId);

		if(match.getWinner() != null) {
			logger.debug("Finished match, match id: {}", matchId);
			throw new MatchFinishedException(matchId);
		}
		if(!match.getTurnPlayerId().equals(player)) {
			logger.debug("Incorrect player turn, match id: {}, current player turn id: {}, playerid: {}", matchId, match.getTurnPlayerId(), player);
			throw new IncorrectTurnException(matchId, player, match.getTurnPlayerId());
		}
		if(!match.hasStones(player, pit)) {
			logger.debug("Empty pit, player id: {}, pit id: {}, match id: {}", player, pit, matchId);
			throw new EmptyPitException(matchId, player, pit);
		}
	}
	
	private void validateExistingMatch(Match match, Integer matchId) {
		if(match == null) {
			logger.debug("Match not found, match id: {}", matchId);
			throw new NotFoundMatchException(matchId);
		}
	}

	public Match getMatch(Integer id) {
		Match match = matchRepository.findOne(id);
		validateExistingMatch(match, id);
		logger.debug("Get match, match id: {}", id);
		return match;
	}

	public void deleteMatch(Integer id) {
		Match match = matchRepository.findOne(id);
		validateExistingMatch(match, id);
		matchRepository.delete(id);
		logger.debug("Match deleted, match id: {}", id);
	}
}
