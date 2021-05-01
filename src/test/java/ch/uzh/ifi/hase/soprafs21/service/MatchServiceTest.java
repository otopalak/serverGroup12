package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.Matches;
import ch.uzh.ifi.hase.soprafs21.repository.MatchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @InjectMocks
    private MatchService matchService;

    private Matches match;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        match = new Matches();
        match.setId(1L);
        match.setItemIdOne((long) 1);
        match.setItemIdTwo((long) 2);

        Mockito.when(matchRepository.save(Mockito.any())).thenReturn(match);

    }

    @Test
    void createMatch_successful() {
        Matches createdMatch = matchService.createMatch((long) 1, (long) 2);

        Mockito.verify(matchRepository, Mockito.times(1)).save(Mockito.any());

        //assertEquals(match.getId(), createdMatch.getId());
        assertEquals(match.getItemIdOne(), createdMatch.getItemIdOne());
        assertEquals(match.getItemIdTwo(), createdMatch.getItemIdTwo());
    }
    @Test
    void createMatch_createDublicateMatch() {
        Matches dubMatch = new Matches();
        dubMatch.setId(1L);
        // inverted itemID's
        dubMatch.setItemIdOne((long) 2);
        dubMatch.setItemIdTwo((long) 1);

        //assume match is already stored in the repo
        List<Matches> allMatches = new ArrayList<>();
        allMatches.add(match);

        given(matchRepository.findAll()).willReturn(allMatches);

        Matches returnedMatch = matchService.createMatch((long) 2, (long) 1);

        // same match should not be saved again
        Mockito.verify(matchRepository, Mockito.times(0)).save(Mockito.any());

        assertEquals(match.getId(), returnedMatch.getId());
    }

    @Test
    void getAllMatchesByItemID_givenItemIdOne() {
        List<Matches> allMatches = new ArrayList<>();
        allMatches.add(match);
        List<Matches> emptyList = new ArrayList<>();

        long itemIdOne = match.getItemIdOne();
        long itemIdTwo = match.getItemIdTwo();

        given(matchRepository.findByItemIdOne(match.getItemIdOne())).willReturn(allMatches);
        given(matchRepository.findByItemIdTwo(match.getItemIdTwo())).willReturn(emptyList);

        List<Matches> foundMatches = matchService.getAllMatchesByItemID(itemIdOne);
        assertEquals(match.getId() ,foundMatches.get(0).getId());
    }

    @Test
    void getAllMatchesByItem() {
        //Method not used yet
    }

    @Test
    void getMatchByMatchID() {
        given(matchRepository.findById(2L)).willReturn(match);

        Matches returnedMatch = matchService.getMatchByMatchID(2L);
        assertEquals(match.getId(),returnedMatch.getId());

    }
}