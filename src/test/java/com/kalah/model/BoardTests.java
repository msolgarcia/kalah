package com.kalah.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class BoardTests {

	@Test
	public void TestSowStones() {
		Board board = new Board();
		
		boolean additionalTurn = board.sowStones(1, 0);
		assertTrue(additionalTurn);
		
		assertEquals(0, board.getStones(1, 0)); //picked stones
		for(int i = 1; i < 6 ; i++) {
			assertEquals(7, board.getStones(1, i));
		}
		assertEquals(1, board.getKalahs().get(1).intValue());
		assertEquals(0, board.getKalahs().get(2).intValue());
		
		for(int i = 0; i < 6 ; i++) {
			assertEquals(6, board.getStones(2, i));
		}
		
		assertEquals(35, board.getSumPits().get(1).intValue());
		assertEquals(36, board.getSumPits().get(2).intValue());
		
	}

	
	@Test
	public void TestSowStones_CompleteRound() {
		Board board = getBoardForCompleteRoundTest();
		
		boolean additionalTurn = board.sowStones(2, 1);
		
		assertFalse(additionalTurn);
		
		assertEquals(11, board.getKalahs().get(1).intValue());
		assertEquals(11, board.getKalahs().get(2).intValue());
		
		assertStonesAfterCompleteRound(board);
		
	}
	
	private void assertStonesAfterCompleteRound(Board board) {
		assertEquals(1, board.getStones(1, 0));
		assertEquals(4, board.getStones(1, 1));
		assertEquals(1, board.getStones(1, 2));
		assertEquals(2, board.getStones(1, 3));
		assertEquals(14, board.getStones(1, 4));
		assertEquals(2, board.getStones(1, 5));
		assertEquals(12, board.getStones(2, 0));
		assertEquals(0, board.getStones(2, 1)); //picked stones
		assertEquals(2, board.getStones(2, 2));
		assertEquals(10, board.getStones(2, 3));
		assertEquals(1, board.getStones(2, 4));
		assertEquals(1, board.getStones(2, 5));
	}
	
	private Board getBoardForCompleteRoundTest() {
		Board board = new Board();
		
		Map<Integer, PlayerPits> pits = board.getPits();
		pits.get(1).setStones(0, 0);
		pits.get(1).setStones(1, 3);
		pits.get(1).setStones(2, 0);
		pits.get(1).setStones(3, 1);
		pits.get(1).setStones(4, 13);
		pits.get(1).setStones(5, 1);
		pits.get(2).setStones(0, 11);
		pits.get(2).setStones(1, 12);
		pits.get(2).setStones(2, 1);
		pits.get(2).setStones(3, 9);
		pits.get(2).setStones(4, 0);
		pits.get(2).setStones(5, 0);
		
		board.getKalahs().put(1, 11);
		board.getKalahs().put(2, 10);
		
		board.getSumPits().put(1, 18);
		board.getSumPits().put(2, 33);
		
		return board;
	}
	
	@Test
	public void TestSowStones_CompleteRoundWithCapture() {
		Board board = getBoardForCompleteRoundTest();
		
		Map<Integer, PlayerPits> pits = board.getPits();
		
		pits.get(2).setStones(0, 10);
		pits.get(2).setStones(1, 13);
		
		boolean additionalTurn = board.sowStones(2, 1);
		
		assertFalse(additionalTurn);
		
		assertEquals(11, board.getKalahs().get(1).intValue());
		assertEquals(26, board.getKalahs().get(2).intValue());
		
		assertStonesAfterCompleteRoundWithCapture(board);
		
	}
	
	private void assertStonesAfterCompleteRoundWithCapture(Board board) {
		assertEquals(1, board.getStones(1, 0));
		assertEquals(4, board.getStones(1, 1));
		assertEquals(1, board.getStones(1, 2));
		assertEquals(2, board.getStones(1, 3));
		assertEquals(0, board.getStones(1, 4)); //captured stones
		assertEquals(2, board.getStones(1, 5));
		assertEquals(11, board.getStones(2, 0));
		assertEquals(0, board.getStones(2, 1)); //picked stones
		assertEquals(2, board.getStones(2, 2));
		assertEquals(10, board.getStones(2, 3));
		assertEquals(1, board.getStones(2, 4));
		assertEquals(1, board.getStones(2, 5));
	}
	
	@Test
	public void TestSowStones_CapturRivalStones() {
		Board board = new Board();
		
		Map<Integer, PlayerPits> pits = board.getPits();
		pits.get(1).setStones(1, 3);
		pits.get(1).setStones(4, 0);
		pits.get(1).setStones(5, 14);
		
		boolean additionalTurn = board.sowStones(1, 1);
		assertFalse(additionalTurn);
		
		assertEquals(0, board.getStones(1, 1)); //picked stones
		
		assertEquals(7, board.getKalahs().get(1).intValue()); // captured 6 stones from rival plus player's stone
		assertEquals(0, board.getKalahs().get(2).intValue());
		
		assertEquals(0, board.getStones(2, 1)); //captured rival stones
		assertEquals(6, board.getStones(2, 0));
		for(int i = 2; i < 6 ; i++) {
			assertEquals(6, board.getStones(2, i));
		}
		
	}
	
	@Test
	public void TestGetWinner() {
		Board board = new Board();
		
		assertEquals(0, board.getWinner().intValue());
		
		board.getKalahs().put(1, 10);
		board.getKalahs().put(2, 2);
		
		assertEquals(1, board.getWinner().intValue());
		
		board.getKalahs().put(2, 10);
		board.getKalahs().put(1, 2);
		
		assertEquals(2, board.getWinner().intValue());
	}
	
	@Test
	public void TestMoveStonesToKalah() {
		Board board = new Board();
		
		board.moveStonesToKalah();
		
		for(int i = 0 ; i < 6 ; i++) {
			assertEquals(0, board.getStones(1, i));
			assertEquals(0, board.getStones(2, i));
		}
		
		assertEquals(36, board.getKalahs().get(1).intValue());
		assertEquals(36, board.getKalahs().get(2).intValue());	
	}
	
	@Test
	public void TestMoveStonesToKalah_PlayedBoard() {
		Board board = new Board();
		
		Map<Integer, PlayerPits> pits = board.getPits();
		pits.get(1).setStones(0, 0);
		pits.get(1).setStones(4, 0);
		pits.get(1).setStones(5, 14);
		
		
		board.moveStonesToKalah();
		
		for(int i = 0 ; i < 6 ; i++) {
			assertEquals(0, board.getStones(1, i));
			assertEquals(0, board.getStones(2, i));
		}
		
		assertEquals(36, board.getKalahs().get(1).intValue());
		assertEquals(36, board.getKalahs().get(2).intValue());	
	}
	

}
