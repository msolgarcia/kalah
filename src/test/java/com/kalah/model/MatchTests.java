package com.kalah.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MatchTests {

	@Mock
	Board board;
	
	@Test
	public void TestHasStones() {
		Match match = new Match("one", "two");
		Map<Integer, PlayerPits> pits = match.getBoard().getPits();
		pits.get(1).setStones(0, 0);
		
		assertFalse(match.hasStones(1, 0));
		assertTrue(match.hasStones(1, 1));
		
	}
	
	@Test
	public void TestSowsStones() {
		
		Match match = new Match("one", "two");
		match.setBoard(board);
		
		when(board.sowStones(1, 1)).thenReturn(false);
		when(board.hasEmptyPits()).thenReturn(false);
		
		match.sowsStones(1, 1);
		
		assertEquals(2, match.getTurnPlayerId().intValue());
		assertNull(match.getWinner());		
	}
	
	@Test
	public void TestSowsStones_AdditionalTurn() {
		
		Match match = new Match("one", "two");
		match.setBoard(board);
		
		when(board.sowStones(1, 1)).thenReturn(true);
		when(board.hasEmptyPits()).thenReturn(false);
		
		match.sowsStones(1, 1);
		
		assertEquals(1, match.getTurnPlayerId().intValue());
		assertNull(match.getWinner());		
	}
	
	@Test
	public void TestSowsStones_EndGame() {
		
		Match match = new Match("one", "two");
		match.setBoard(board);
		
		when(board.sowStones(1, 1)).thenReturn(false);
		when(board.hasEmptyPits()).thenReturn(true);
		when(board.getWinner()).thenReturn(1);
		
		match.sowsStones(1, 1);
		
		assertEquals(1, match.getTurnPlayerId().intValue());
		assertEquals(1, match.getWinner().intValue());
	}

}
