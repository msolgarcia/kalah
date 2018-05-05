package com.kalah.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kalah.web.controller.exception.InternalKalahException;

@Entity
public class PlayerPits {
	
	final static Logger logger = LoggerFactory.getLogger(PlayerPits.class);
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne
	private Board board;
	
	@ElementCollection(fetch=FetchType.EAGER)
	@MapKeyColumn(name="pit_id")
    @Column(name="stones_num")
    @CollectionTable(name="pits_stones", joinColumns=@JoinColumn(name="player_pits_id"))
	private Map<Integer, Integer> pits;
	
	public PlayerPits() {}
	
	public PlayerPits(Board board, Integer pitsLenght, Integer initialStones) {
		this.board = board;
		pits = new HashMap<>(pitsLenght);
		for(int i= 0 ; i < pitsLenght ; i++) {
			pits.put(i, initialStones);
		}
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Map<Integer, Integer> getPits() {
		return pits;
	}

	public void setPits(Map<Integer, Integer> pits) {
		this.pits = pits;
	}
	
	public Integer getStones(Integer pit) {
		if (!pits.containsKey(pit)) {
			logger.error("Pits index out of bounds. pit index: {}", pit);
			throw new InternalKalahException();
		}
		return pits.get(pit);
	}
	
	public void setStones(Integer pit, Integer stones) {
		pits.put(pit, stones);
	}

	
	public void distribute(int from, int to) {
		if (from < 0 || to >= pits.size()) {
			logger.error("Pits index out of bounds. From index: {}, To index: {}", from, to);
			throw new InternalKalahException();
		}
		for(int i = from ; i <= to ; i++) {
			int stonesInPit = pits.get(i);
			pits.put(i, stonesInPit + 1);
		}
	}

	@Override
	public String toString() {
		return pits.toString();
	}
}
