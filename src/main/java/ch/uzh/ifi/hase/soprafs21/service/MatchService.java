package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.Item;
import ch.uzh.ifi.hase.soprafs21.entity.Matches;
import ch.uzh.ifi.hase.soprafs21.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service ("matchService")
@Transactional
public class MatchService {
    private final MatchRepository matchRepository;

    // Checking build again
    @Autowired
    public MatchService(@Qualifier ("matchRepository") MatchRepository matchRepository){
        this.matchRepository = matchRepository;
    }
    public Matches createMatch(long itemIdOne, long itemIdTwo){
        //check for existing match
        //actually this is redundant, because a match can only be created by two Likes, which are both tested for uniqueness already...
        for (Matches match: matchRepository.findAll()){
            if((match.getItemIdOne() == itemIdOne && match.getItemIdTwo() == itemIdTwo) ||
                match.getItemIdOne() == itemIdTwo && match.getItemIdTwo() == itemIdOne){
                return match;
            }
        }
        //create new Match if it does not yet exist
        Matches newMatch = new Matches();
        newMatch.setItemIdOne(itemIdOne);
        newMatch.setItemIdTwo(itemIdTwo);

        //add Match to DB
        matchRepository.save(newMatch);
        matchRepository.flush();
        return newMatch;
    }

    public String deleteMatch(long matchID){
        Matches match = matchRepository.findById(matchID);
        if (match == null) {
            String baseErrorMessage = "The match with this id does not exist";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage));
        }
        else {
            matchRepository.deleteById(matchID);
            return "unmatch Successfull";
        }
    }

    public List<Matches> getAllMatchesByItemID(long itemId){
        List<Matches> fromIdOne = matchRepository.findByItemIdOne(itemId);
        List<Matches> fromIdTwo = matchRepository.findByItemIdTwo(itemId);
        return Stream.concat(fromIdOne.stream(), fromIdTwo.stream())
                .collect(Collectors.toList());
    }

    public List<Matches> getAllMatchesByItem(Item item){
        long itemId = item.getId();
        return getAllMatchesByItemID(itemId);
    }

    public Matches getMatchByMatchID(long matchID){
        return matchRepository.findById(matchID);
    }
}
