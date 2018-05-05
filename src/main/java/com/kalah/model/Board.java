package com.kalah.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kalah.web.controller.exception.InternalKalahException;

@Entity
public class Board {
	
	final static Logger logger = LoggerFactory.getLogger(Board.class);
	
	private static final int TOTAL_STONES = 6;
	private static final int TOTAL_PITS = 6;
	private static final int PLAYER_ONE = 1;
	private static final int PLAYER_TWO = 2;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	
	@OneToMany(mappedBy = "board", cascade = CascadeType.ALL)  
    @MapKeyColumn(name = "player_id")
	private Map<Integer, PlayerPits> pits;
	
	@ElementCollection
    @MapKeyColumn(name="player_id")
    @Column(name="total_stones_pits")
    @CollectionTable(name="sum_pits", joinColumns=@JoinColumn(name="board_id"))
	private Map<Integer, Integer> sumPits;
	
	@ElementCollection
    @MapKeyColumn(name="player_id")
    @Column(name="stones_num")
    @CollectionTable(name="stones_kalah", joinColumns=@JoinColumn(name="board_id"))
	private Map<Integer, Integer> kalahs;
	
	public Board() {
		pits = new HashMap<Integer, PlayerPits>(2);
		pits.put(PLAYER_ONE, new PlayerPits(this, TOTAL_PITS, TOTAL_STONES));
		pits.put(PLAYER_TWO, new PlayerPits(this, TOTAL_PITS, TOTAL_STONES));
		
		kalahs = new HashMap<Integer, Integer>(2);
		kalahs.put(PLAYER_ONE, 0);
		kalahs.put(PLAYER_TWO, 0);
		
		sumPits = new HashMap<Integer, Integer>(2);
		
		sumPits.put(PLAYER_ONE, TOTAL_PITS * TOTAL_STONES);
		sumPits.put(PLAYER_TWO, TOTAL_PITS * TOTAL_STONES);
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public Map<Integer, PlayerPits> getPits() {
		return pits;
	}

	public void setPits(Map<Integer, PlayerPits> pits) {
		this.pits = pits;
	}

	public Map<Integer, Integer> getSumPits() {
		return sumPits;
	}

	public void setSumPits(Map<Integer, Integer> sumPits) {
		this.sumPits = sumPits;
	}

	public Map<Integer, Integer> getKalahs() {
		return kalahs;
	}

	public void setKalahs(Map<Integer, Integer> kalahs) {
		this.kalahs = kalahs;
	}

	public int getStones(Integer player, Integer pit) {
		return pits.get(player).getStones(pit);
	}

	private int pickStones(Integer player, Integer pit) {
		int stonesToDistribute = pits.get(player).getStones(pit);
		pits.get(player).setStones(pit, 0);
		sumPits.put(player, sumPits.get(player) - stonesToDistribute);
		return stonesToDistribute;
	}
	
	private int distributeForPlayer(Integer player, Integer from, Integer to) {
		pits.get(player).distribute(from, to);
		int distributed = to - (from - 1);
		sumPits.put(player, sumPits.get(player) + distributed);
		return distributed;
	}
	
	private int distributeForRival(int rival, int stonesToDistribute) {
		int toRival = stonesToDistribute >= TOTAL_PITS ? TOTAL_PITS - 1 : stonesToDistribute - 1;	
		sumPits.put(rival, sumPits.get(rival) + (toRival + 1));
		pits.get(rival).distribute(0, toRival);
		return toRival + 1;
	}
	
	private int getRival(int player) {
		return player == 1 ? 2 : 1;
	}
	
	private int calculateToPit(int stonesToDistribute, int pit) {
		return (pit + stonesToDistribute) < TOTAL_PITS ? pit + stonesToDistribute : TOTAL_PITS - 1;
	}
	
	public boolean sowStones(Integer player, Integer pit) {
		
		int stonesToDistribute = pickStones(player, pit);
		int rival = getRival(player);
		
		while(stonesToDistribute > 0) {
			int to = calculateToPit(stonesToDistribute, pit);
			stonesToDistribute = stonesToDistribute - distributeForPlayer(player, pit + 1, to);
			if(stonesToDistribute > 0) {
				kalahs.put(player, kalahs.get(player) + 1);
				stonesToDistribute --;
				if(stonesToDistribute > 0) {
					stonesToDistribute = stonesToDistribute - distributeForRival(rival, stonesToDistribute);
					pit = -1;
				} else {
					return true;
				}
			} else if(pits.get(player).getStones(to) == 1) {
				moveRivalStones(player, rival, to);	// Capture rival stones
			}
		}
		
		hasAllStones();
		
		return false;
	}
	
	private void hasAllStones() {
		int allStones = sumPits.get(1) + sumPits.get(2) 
				+ kalahs.get(1) + kalahs.get(2);
		
		if(allStones != TOTAL_STONES * TOTAL_PITS * 2) {
			logger.error("Total stones is not correct after sow stones, current stones number {}", allStones);
			throw new InternalKalahException();
		}
	}
	
	private void moveRivalStones(int player, int rival, int to) {
		int opposite = to - (TOTAL_PITS - 1 );
		opposite = opposite >= 0 ? opposite : -opposite;
		int stonesOpposite = pits.get(rival).getStones(opposite);
		if (stonesOpposite > 0) {

			logger.debug("Captured rival stones. Player id: {}, Rival id: {}, captured stones: {}", player, rival, stonesOpposite);

			pits.get(player).setStones(to, 0);
			sumPits.put(player, sumPits.get(player) - 1); 
			pits.get(rival).setStones(opposite, 0);
			kalahs.put(player, kalahs.get(player) + 1 + stonesOpposite);
			sumPits.put(rival, sumPits.get(rival) - stonesOpposite);			
		}
	}

	public boolean hasEmptyPits() {
		return sumPits.get(PLAYER_ONE) == 0 || sumPits.get(PLAYER_TWO) == 0;
	}

	public void moveStonesToKalah() {
		for(int i= 0 ; i < TOTAL_PITS ; i++) {
			pits.get(PLAYER_ONE).setStones(i, 0);
			pits.get(PLAYER_TWO).setStones(i, 0);
		}
		kalahs.put(PLAYER_ONE, kalahs.get(PLAYER_ONE) + sumPits.get(PLAYER_ONE));
		kalahs.put(PLAYER_TWO, kalahs.get(PLAYER_TWO) + sumPits.get(PLAYER_TWO));
		sumPits.put(PLAYER_ONE, 0);
		sumPits.put(PLAYER_TWO, 0);
	}

	public Integer getWinner() {
		return kalahs.get(PLAYER_ONE) == kalahs.get(PLAYER_TWO) ? 0 : kalahs.get(PLAYER_ONE) > kalahs.get(PLAYER_TWO) ? PLAYER_ONE : PLAYER_TWO;
	}

	@Override
	public String toString() {
		
		return "\nPits player one: " + pits.get(PLAYER_ONE).toString() + " Kalah 1: " + kalahs.get(PLAYER_ONE)
				+ " --- Pits player two: " + pits.get(PLAYER_TWO).toString() + " Kalah 2: " + kalahs.get(PLAYER_TWO)
				+ " --- Sum pits: " + sumPits.toString();
	}
}
