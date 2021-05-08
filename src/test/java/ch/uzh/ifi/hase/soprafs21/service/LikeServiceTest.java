package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.Like;
import ch.uzh.ifi.hase.soprafs21.entity.Matches;
import ch.uzh.ifi.hase.soprafs21.repository.LikeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;
    @Mock
    private MatchService matchService;

    @InjectMocks
    private LikeService likeService;
    private Like trueLike1;
    private Like trueLike2;
    private Like falseLike1;
    private Matches match;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        trueLike1 = new Like();
        trueLike1.setItemIDSwiper((long) 1);
        trueLike1.setItemIDSwiped((long) 2);
        trueLike1.setLiked(true);

        trueLike2 = new Like();
        trueLike2.setItemIDSwiper((long) 2);
        trueLike2.setItemIDSwiped((long) 1);
        trueLike2.setLiked(true);

        falseLike1 = new Like();
        falseLike1.setItemIDSwiper((long) 1);
        falseLike1.setItemIDSwiped((long) 2);
        falseLike1.setLiked(false);

        match = new Matches();
        match.setItemIdOne(trueLike1.getLikeID());
        match.setItemIdTwo(trueLike2.getLikeID());

        Mockito.when(likeRepository.save(trueLike1)).thenReturn(trueLike1);
        Mockito.when(likeRepository.save(trueLike2)).thenReturn(trueLike2);
        Mockito.when(likeRepository.save(trueLike1)).thenReturn(trueLike1);
    }

    @Test
    public void createLike() {
        Like repoLike = likeService.createLike(trueLike1);
        //check that the Like is exactly saved into the database once
        Mockito.verify(likeRepository, Mockito.times(1)).save(Mockito.any());
        assertEquals(trueLike1, repoLike);
    }

    @Test
    public void createLike_createSameLikeTwice() {
        Like repoLike = likeService.createLike(falseLike1);

        List<Like> repo = new ArrayList<>();
        repo.add(falseLike1);

        //create dublicate like with same properties as falseLike1
        Like sameLike = new Like();
        sameLike.setItemIDSwiper((long) 1);
        sameLike.setItemIDSwiped((long) 2);
        sameLike.setLiked(false);

        //create dublicate will first look for existing Like entities
        Mockito.when(likeRepository.findAll()).thenReturn(repo);
        Like likeAfterDublicate = likeService.createLike(sameLike);

        //For two Likes with same properties, it should only be saved once in the database
        Mockito.verify(likeRepository, Mockito.times(1)).save(Mockito.any());
        assertEquals(repoLike, likeAfterDublicate);
    }

    @Test
    void checkLikeForMatch_matchCreated() {
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //assume trueLike1 is in the likeRepo
        Mockito.when(likeRepository.findByItemIDSwipedAndItemIDSwiper(Mockito.anyLong(),Mockito.anyLong())).thenReturn(trueLike1);
        //--> createMatch is called once
        likeService.checkLikeForMatch(trueLike2);
        //Mockito.verify(matchService, Mockito.times(2)).createMatch(Mockito.any(),Mockito.any());

    }
}