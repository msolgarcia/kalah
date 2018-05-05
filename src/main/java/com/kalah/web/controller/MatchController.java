package com.kalah.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kalah.model.Match;
import com.kalah.service.MatchService;

@RestController
public class MatchController {
	
	@Autowired
	private MatchService service;
	
	@RequestMapping(method = RequestMethod.GET, path = "/match")
    public List<MatchResponse> getMatches() {
        return generateMatchListResponse(service.getMatches());
    }
	
	private List<MatchResponse> generateMatchListResponse(List<Match> matches) {
		
		if(matches == null) {
			return null;
		}
			
		List<MatchResponse> responseList = new ArrayList<MatchResponse>();
		for(Match match : matches) {
			responseList.add(new MatchResponse(match));
		}
		return responseList;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/match/{id}")
    public MatchResponse getMatch(@PathVariable (name="id") @NotNull Integer id) {
        return new MatchResponse(service.getMatch(id));
    }
	
	@RequestMapping(method = RequestMethod.DELETE, path = "/match/{id}")
    public void discardMatch(@PathVariable (name="id") @NotNull Integer id) {
        service.deleteMatch(id);
    }
	
	@RequestMapping(method = RequestMethod.POST, path = "/match")
    public MatchResponse addMatch(@Valid @RequestBody NewMatch newMatch) {
        return new MatchResponse(service.createMatch(newMatch.getPlayerOneName(), newMatch.getPlayerTwoName()));
    }
	
	@RequestMapping(method = RequestMethod.POST, path = "/match/{id}/play")
    public MatchResponse newPlay(@PathVariable (name="id") @NotNull Integer id, @Valid @RequestBody Play play) {
		
        return new MatchResponse(service.play(id, play.getPlayer(), play.getPit())); 
    }
}
