package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.repository.*;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ChatMessageServiceIntegrationTest {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private MatchRepository matchRepository;


    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private TagsRepository tagsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemService itemService;

    @Autowired
    private TagsService tagsService;

    @Autowired
    private MatchService matchService;

    @Autowired
    private ChatMessageService chatMessageService;

    private User user;
    private User user2;
    private Tags tag;
    private Item item;
    private Item item2;
    private Matches matches;

    @BeforeEach
    public void setup()

    {
        //create User 1
        user = new User();
        user.setPassword("password");
        user.setName("mauro");
        user.setUsername("username");
        user.setStatus(UserStatus.ONLINE);
        user.setToken("tokessn");

        //create User2
        user2 = new User();
        user2.setPassword("password");
        user2.setName("dennis");
        user2.setUsername("dennis");



        user2.setStatus(UserStatus.ONLINE);
        user2.setToken("toksssssen");

        //create tag 1
        tag = new Tags();
        tag.setDescription("Test");

        //create Item 1
        item = new Item();
        item.setDescription("Test Description");
        item.setTitle("Title");
        List<Tags> tags = new ArrayList<>();
        tags.add(tag);
        item.setItemtags(tags);

        //create Item2
        item2 = new Item();
        item2.setDescription("Test 2 Description");
        item2.setTitle("Title2");
        List<Tags> tags2 = new ArrayList<>();
        tags2.add(tag);
        item2.setItemtags(tags2);


    }

    @Test
    public void checkSave(){
        User savedUser = userRepository.save(user);
        User savedUser2 = userRepository.save(user2);
        tagsRepository.save(tag);
        item.setUserId(savedUser.getId());
        item2.setUserId(savedUser2.getId());
        Item savedItem = itemRepository.save(item);
        Item savedItem2 =itemRepository.save(item2);

        //Before
        // Create Matches
        matches = new Matches();
        matches.setItemIdOne(savedItem.getId());
        matches.setItemIdTwo(savedItem2.getId());
        matchRepository.save(matches);



        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent("Hello this is a chatmessage");
        chatMessage.setMatchId(matches.getId());
        chatMessage.setRecipientId(matches.getItemIdOne());
        chatMessage.setSenderId(matches.getItemIdTwo());


        ChatMessage chatMessage1 = chatMessageService.save(chatMessage);

        assertEquals(chatMessage1.getContent(),chatMessage.getContent());
        assertEquals(chatMessage1.getMatchId(),chatMessage.getMatchId());

    }
    /*@Test
    public void check_countNewMessages(){
        User savedUser = userRepository.save(user);
        User savedUser2 = userRepository.save(user2);
        tagsRepository.save(tag);
        item.setUserId(savedUser.getId());
        item2.setUserId(savedUser2.getId());
        Item savedItem = itemRepository.save(item);
        Item savedItem2 =itemRepository.save(item2);

        //Before
        // Create Matches
        matches = new Matches();
        matches.setItemIdOne(savedItem.getId());
        matches.setItemIdTwo(savedItem2.getId());
        matchRepository.save(matches);

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent("Hello this is a chatmessage");
        chatMessage.setMatchId(matches.getId());
        chatMessage.setRecipientId(matches.getItemIdOne());
        chatMessage.setRecipientName("dennis");
        chatMessage.setSenderId(item2.getId());


        ChatMessage chatMessage1 = chatMessageService.save(chatMessage);
        Long amount = chatMessageService.countNewMessages(matches.getId());

        assert(amount==1);

    }*/
    @Test
    public void check_findchatmessages(){
        User savedUser = userRepository.save(user);
        User savedUser2 = userRepository.save(user2);
        tagsRepository.save(tag);
        item.setUserId(savedUser.getId());
        item2.setUserId(savedUser2.getId());
        Item savedItem = itemRepository.save(item);
        Item savedItem2 =itemRepository.save(item2);

        //Before
        // Create Matches
        matches = new Matches();
        matches.setItemIdOne(savedItem.getId());
        matches.setItemIdTwo(savedItem2.getId());
        matchRepository.save(matches);

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent("Hello this is a chatmessage");

        chatMessage.setMatchId(matches.getId());
        chatMessage.setRecipientId(matches.getItemIdOne());
        chatMessage.setRecipientName("dennis");
        chatMessage.setSenderId(item2.getId());

        ChatMessage chatMessage1 = chatMessageService.save(chatMessage);

        List<ChatMessage> messagesPerChat= new ArrayList<>();
        messagesPerChat = chatMessageService.findChatMessages(matches.getId());

        assertEquals(chatMessage1.getContent(),messagesPerChat.get(0).getContent());
    }

    @Test
    public void findById_check_noSuccess(){
        User savedUser = userRepository.save(user);
        User savedUser2 = userRepository.save(user2);
        tagsRepository.save(tag);
        item.setUserId(savedUser.getId());
        item2.setUserId(savedUser2.getId());
        Item savedItem = itemRepository.save(item);
        Item savedItem2 =itemRepository.save(item2);

        //Before
        // Create Matches
        matches = new Matches();
        matches.setItemIdOne(savedItem.getId());
        matches.setItemIdTwo(savedItem2.getId());
        matchRepository.save(matches);

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent("Hello this is a chatmessage");
        //chatMessage.setStatus(MessageStatus.RECEIVED);
        chatMessage.setMatchId(matches.getId());
        chatMessage.setRecipientId(matches.getItemIdOne());
        chatMessage.setRecipientName("dennis");
        chatMessage.setSenderId(item2.getId());

        ChatMessage chatMessage1 = chatMessageService.save(chatMessage);



        // check that an error is thrown
        assertThrows(NotFoundException.class, () -> chatMessageService.findById(2L));
    }

    @Test
    public void findById_check_Success() throws NotFoundException {
        User savedUser = userRepository.save(user);
        User savedUser2 = userRepository.save(user2);
        tagsRepository.save(tag);
        item.setUserId(savedUser.getId());
        item2.setUserId(savedUser2.getId());
        Item savedItem = itemRepository.save(item);
        Item savedItem2 =itemRepository.save(item2);

        //Before
        // Create Matches
        matches = new Matches();
        matches.setItemIdOne(savedItem.getId());
        matches.setItemIdTwo(savedItem2.getId());
        matchRepository.save(matches);

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent("Hello this is a chatmessage");
        //chatMessage.setStatus(MessageStatus.RECEIVED);
        chatMessage.setMatchId(matches.getId());
        chatMessage.setRecipientId(matches.getItemIdOne());
        chatMessage.setRecipientName("dennis");
        chatMessage.setSenderId(item2.getId());

        ChatMessage chatMessage1 = chatMessageService.save(chatMessage);
        ChatMessage chatMessage2 = chatMessageService.findById(chatMessage1.getId());

        assertEquals(chatMessage2.getId(), chatMessage1.getId());




    }


}
