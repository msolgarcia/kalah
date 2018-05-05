package com.kalah.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.kalah.model.Match;

public interface MatchRepository extends CrudRepository<Match, Integer>{

	List<Match> findAll();
	Match findOne(Integer id);
}