package com.kalah.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.kalah.web.controller.exception.InternalKalahException;

@RunWith(SpringRunner.class)
public class PlayerPitsTest {

	@Test
	public void TestDistribute() {
		
		PlayerPits pits = new PlayerPits(null, 6, 6);
		pits.distribute(0, 5);
		
		for(int i = 0 ; i < 6 ; i++) {
			assertEquals(7, pits.getStones(i).intValue());
		}
	}
	
	@Test(expected = InternalKalahException.class)
	public void TestDistributeOutOfBoundsTo() {
		
		PlayerPits pits = new PlayerPits(null, 6, 6);
		pits.distribute(0, 6);
	}
	
	@Test(expected = InternalKalahException.class)
	public void TestDistributeOutOfBoundsFrom() {
		
		PlayerPits pits = new PlayerPits(null, 6, 6);
		
		pits.distribute(-1, 5);
	}

}
