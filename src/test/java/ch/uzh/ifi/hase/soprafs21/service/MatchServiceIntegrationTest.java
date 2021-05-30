package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MatchServiceIntegrationTest {

    @Qualifier("itemRepository")
    @Autowired
    private ItemRepository itemRepository;

    @Qualifier("tagsRepository")
    @Autowired
    private TagsRepository tagsRepository;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Qualifier("likeRepository")
    @Autowired
    private LikeRepository likeRepository;

    @Qualifier("matchRepository")
    @Autowired
    private MatchRepository matchRepository;

    @Qualifier("swapConfirmationRepository")
    @Autowired
    private SwapConfirmationRepository swapConfirmationRepository;

    @Autowired
    private MatchService matchService;

    private User user1;
    private User user2;
    private Tags tag;
    private Item item1;
    private Item item2;
    private Like like1;
    private Like like2;
    private Matches match;

    private User savedUser1;
    private User savedUser2;
    private Tags savedTag;
    private Item savedItem1;
    private Item savedItem2;
    private Like savedLike1;
    private Like savedLike2;

    @BeforeEach
    void setUp() {
        //create User1
        user1 = new User();
        user1.setPassword("password");
        user1.setName("mauro");
        user1.setUsername("username");

        user1.setStatus(UserStatus.ONLINE);
        user1.setToken("token");

        //create User2
        user2 = new User();
        user2.setPassword("password2");
        user2.setName("nico");
        user2.setUsername("username2");

        user2.setStatus(UserStatus.ONLINE);
        user2.setToken("token2");

        //create tag
        tag = new Tags();
        tag.setDescription("Test");
        List<Tags> tags = new ArrayList<>();
        tags.add(tag);

        savedTag = tagsRepository.save(tag);
        tagsRepository.flush();

        //create Item1
        item1 = new Item();
        item1.setDescription("Test Description");
        item1.setTitle("Title");
        item1.setItemtags(tags);

        //create Item2
        item2 = new Item();
        item2.setDescription("Test Description");
        item2.setTitle("Title");
        item2.setItemtags(tags);

        //set up db
        savedUser1 = userRepository.save(user1);
        savedUser2 = userRepository.save(user2);
        userRepository.flush();

        item1.setUserId(savedUser1.getId());
        item2.setUserId(savedUser2.getId());

        savedItem1 = itemRepository.save(item1);
        savedItem2 = itemRepository.save(item2);
        itemRepository.flush();

        //create Likes
        like1 = new Like();
        like1.setItemIDSwiper(savedItem1.getId());
        like1.setItemIDSwiped(savedItem2.getId());
        like1.setLiked(true);

        like2 = new Like();
        like2.setItemIDSwiper(savedItem2.getId());
        like2.setItemIDSwiped(savedItem1.getId());
        like2.setLiked(true);

        //save in db
        savedLike1 = likeRepository.save(like1);
        savedLike2 = likeRepository.save(like2);
    }

    @Test
    void createAndDeleteMatch_integrationTest() {
        long itemID1 = savedItem1.getId();
        long itemID2 = savedItem2.getId();

        //check that repo is filled
        assertEquals(userRepository.findAll().size(), 2);
        assertEquals(itemRepository.findAll().size(), 2);
        assertEquals(likeRepository.findAll().size(), 2);
        assertEquals(matchRepository.findAll().size(), 0);

        long matchIDNotExisting = itemID1;
        assertThrows(ResponseStatusException.class, () -> matchService.deleteMatch(itemID1));

        //create Match
        Matches createdMatch = matchService.createMatch(itemID1, itemID2);

        assertEquals(matchRepository.findAll().size(), 1);
        assertEquals(createdMatch.getItemIdOne(), itemID1);
        assertEquals(createdMatch.getItemIdTwo(), itemID2);

        //delete Match
        long matchID = createdMatch.getId();
        String message = matchService.deleteMatch(matchID);
        assertEquals("unmatch successfull", message);

        assertEquals(userRepository.findAll().size(), 2);
        assertEquals(itemRepository.findAll().size(), 2);
        assertEquals(likeRepository.findAll().size(), 0);
        assertEquals(matchRepository.findAll().size(), 0);
    }
}