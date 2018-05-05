package com.kalah.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.kalah.model.Match;
import com.kalah.repository.MatchRepository;
import com.kalah.web.controller.exception.EmptyPitException;
import com.kalah.web.controller.exception.IncorrectTurnException;
import com.kalah.web.controller.exception.MatchFinishedException;
import com.kalah.web.controller.exception.NotFoundMatchException;

@RunWith(MockitoJUnitRunner.class)
public class MatchServiceTests {

	@Mock
	MatchRepository matchRepository;
	
	@Mock
	Match match;
	
	@InjectMocks
	MatchService service;
	
	@Test
	public void TestPlay() {
		
		when(matchRepository.findOne(1)).thenReturn(match);
		when(match.getWinner()).thenReturn(null);
		when(match.getTurnPlayerId()).thenReturn(1);
		when(match.hasStones(1, 1)).thenReturn(true);
		when(matchRepository.save(match)).thenReturn(match);
		
		service.play(1, 1, 1);
		
		verify(matchRepository).findOne(1);
		verify(match).getWinner();
		verify(match).getTurnPlayerId();
		verify(match).hasStones(1, 1);
		verify(matchRepository).save(match);
	}
	
	@Test(expected = MatchFinishedException.class)
	public void TestPlay_FinishedMatch() {
		
		when(matchRepository.findOne(1)).thenReturn(match);
		when(match.getWinner()).thenReturn(1);
		
		service.play(1, 1, 1);
		
		verify(matchRepository).findOne(1);
		verify(match).getWinner();
	}
	
	@Test(expected = IncorrectTurnException.class)
	public void TestPlay_IncorrectTurn() {
		
		when(matchRepository.findOne(1)).thenReturn(match);
		when(match.getWinner()).thenReturn(null);
		when(match.getTurnPlayerId()).thenReturn(2);
		
		service.play(1, 1, 1);
		
		verify(matchRepository).findOne(1);
		verify(match).getWinner();
		verify(match).getTurnPlayerId();
	}
	
	@Test(expected = EmptyPitException.class)
	public void TestPlay_EmptyPit() {
		
		when(matchRepository.findOne(1)).thenReturn(match);
		when(match.getWinner()).thenReturn(null);
		when(match.getTurnPlayerId()).thenReturn(1);
		when(match.hasStones(1, 1)).thenReturn(false);
		
		service.play(1, 1, 1);
		
		verify(matchRepository).findOne(1);
		verify(match).getWinner();
		verify(match).getTurnPlayerId();
		verify(match).hasStones(1, 1);
	}
	
	@Test(expected = NotFoundMatchException.class)
	public void TestPlay_MatchNotFound() {
		
		when(matchRepository.findOne(1)).thenReturn(null);
		
		service.play(1, 1, 1);
		
		verify(matchRepository).findOne(1);
	}
}
