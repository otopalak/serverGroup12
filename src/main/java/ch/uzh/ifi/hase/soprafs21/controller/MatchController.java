package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.Matches;
import ch.uzh.ifi.hase.soprafs21.rest.dto.MatchesGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.MatchService;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MatchController {

    private final MatchService matchService;

    @Autowired
    public MatchController(@Qualifier ("matchService") MatchService matchService){
        this.matchService = matchService;
    }

    @GetMapping("/showmatch/{matchID}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public MatchesGetDTO getMatch(@PathVariable("matchID") long matchID){
        Matches match = matchService.getMatchByMatchID(matchID);

        //
        if (match == null){
            String baseErrorMessage = "Match for given matchID does not exist";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,String.format(baseErrorMessage));
        }
        return DTOMapper.INSTANCE.convertEntityToMatchesGetDTO(match);
    }

    @GetMapping("/{itemID}/showmatches")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<MatchesGetDTO> getAllMatchesByItemID(@PathVariable("itemID") long itemID) {
        List<Matches> allMatches = matchService.getAllMatchesByItemID(itemID);
        List<MatchesGetDTO> matchesGetDTOs = new ArrayList<>();
        for (Matches match: allMatches){
            MatchesGetDTO matchesGetDTO = DTOMapper.INSTANCE.convertEntityToMatchesGetDTO(match);

            //both itemIDs of the MatchGetDTO are swapped, if the first ItemID of the DTO is not the requested itemID
            //this makes it easier for the frontend, because you can always assume, that the first ItemID is the one for which the Matches are requested.
            if(matchesGetDTO.getItemIdOne() != itemID) {
                long temp = matchesGetDTO.getItemIdOne();
                matchesGetDTO.setItemIdOne(itemID);
                matchesGetDTO.setItemIdTwo(temp);
            }
            matchesGetDTOs.add(matchesGetDTO);
        }
        return matchesGetDTOs;
    }
}
