package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Item;
import ch.uzh.ifi.hase.soprafs21.entity.Tags;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.ItemRepository;
import ch.uzh.ifi.hase.soprafs21.repository.TagsRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ItemServiceIntegrationTest {

    @Qualifier("itemRepository")
    @Autowired
    private ItemRepository itemRepository;

    @Qualifier("tagsRepository")
    @Autowired
    private TagsRepository tagsRepository;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemService itemService;

    @Autowired
    private TagsService tagsService;

    private User user;
    private Tags tag;
    private Item item;

    @BeforeEach
    public void setup() {
        //create User
        user = new User();
        user.setId(1L);
        user.setPassword("password");
        user.setName("mauro");
        user.setUsername("username");
        user.setAddress("address");
        user.setPostcode(9050);
        user.setCity("Appenzell");
        user.setStatus(UserStatus.ONLINE);
        user.setToken("token");

        //userRepository.save(user);

        //create tag
        tag = new Tags();
        tag.setDescription("Test");

        //tagsRepository.save(tag);

        //create Item
        item = new Item();
        item.setUserId(user.getId());
        item.setDescription("Test Description");
        item.setTitle("Title");
        List<Tags> tags = new ArrayList<>();
        tags.add(tag);
        item.setItemtags(tags);
    }

    /*
     * Checks the creates a Item function
     */
    @Test
    public void createItem_validInputs_success(){
        userRepository.save(user);
        tagsRepository.save(tag);

        Item itemCreated = itemService.createItem(item);

        assertEquals(item.getId(), itemCreated.getId());
        assertEquals(item.getTitle(), itemCreated.getTitle());
        assertEquals(item.getItemtags(),itemCreated.getItemtags());
    }

    // Checks the function getItemById() throws an error
    @Test
    public void getItemByWrongId_ThrowsError(){
        itemRepository.deleteAll();
        assertTrue(itemRepository.findAll().isEmpty());
        // Create Input ID, that does not exist -> 10L
        User savedUser = userRepository.save(user);
        tagsRepository.save(tag);
        item.setUserId(savedUser.getId());
        itemRepository.save(item);

        try{
            Item foundItem = itemService.getItemById(10L);
            assertEquals(null, foundItem);
            fail();
        }catch (ResponseStatusException e){
        }
    }

    // Successfully get back the Item with correct ID
    @Test
    public void getItemById_CorrectID(){
        //given
        assertNull(itemRepository.findById(1));
        userRepository.save(user);
        tagsRepository.save(tag);

        // save the Item
        itemRepository.save(item);

        assertEquals(itemRepository.findById(item.getId()).get().getId(), item.getId());

//        Item itemToCompare = itemService.getItemById(item.getId());
//
//        assertEquals(item.getId(),itemToCompare.getId());
//        assertEquals(item.getTitle(),itemToCompare.getTitle());
//        assertEquals(item.getDescription(),itemToCompare.getDescription());
    }
    // Testing the deletion of an Item, that throws an error
    @Test
    public void deleteItemById_throwsError(){
        //given
        assertNull(itemRepository.findById(1));
        userRepository.save(user);
        tagsRepository.save(tag);

        // save the Item
        itemRepository.save(item);

        try{
            itemService.deleteItem(10L);
            fail();
        }catch (ResponseStatusException e){
        }
        // Pass in the wrong ID
        //assertThrows(ResponseStatusException.class, () -> itemService.deleteItem(10L));
    }

    // In this test, it is checked, if the deletion of an item works
    @Test
    public void deleteById_success(){
        //given
        assertNull(itemRepository.findById(1));
        userRepository.save(user);
        tagsRepository.save(tag);
        itemRepository.save(item);

        assertTrue(itemRepository.findById(item.getId()).isPresent());

        // We now delete the item
        itemService.deleteItem(item.getId());

        // Assert, that there no more items inside the database
        assertTrue(itemRepository.findById(item.getId()).isEmpty());
    }
    // Testing if we receive all items from
    @Test
    public void returnAllItems(){
        //given
        //assertNull(itemRepository.findById(1));

        // Creating the Items
        userRepository.save(user);
        tagsRepository.save(tag);
        itemRepository.save(item);

        //same user creates another item
        Item item2 = new Item();
        item2.setUserId(user.getId());
        item2.setDescription("Test Description2");
        item2.setTitle("Title2");
        itemRepository.save(item2);
        List <Item> items = itemService.getAllItems();

        assert(items.isEmpty()==false);
    }
    // Throws an error, if the userInput has empty Description
    @Test
    public void updateItem_invalid_withNoDescription() {
        //given
        assertNull(itemRepository.findById(1));
        userRepository.save(user);
        tagsRepository.save(tag);
        itemRepository.save(item);


        Item item2 = new Item();
        item2.setUserId(user.getId());
        item2.setDescription("");
        item2.setTitle("Title2");
        // Has to be saved, otherwise will not run
        itemRepository.save(item2);

        assertThrows(ResponseStatusException.class, () -> itemService.updateItem(item,item2));
    }

    // Throws an error, if the userInput has empty Description
    @Test
    public void updateItem_invalid_withNoTitle() {
        //given
        assertNull(itemRepository.findById(1));
        assertNull(itemRepository.findById(2));

        // Creating the Items
        userRepository.save(user);
        tagsRepository.save(tag);
        itemRepository.save(item);

        Item item2 = new Item();
        item2.setUserId(user.getId());
        item2.setDescription("Description2");
        item2.setTitle("");
        // Has to be saved otherwise won't run somehow
        itemRepository.save(item2);

        assertThrows(ResponseStatusException.class, () -> itemService.updateItem(item,item2));

    }
    // Updates the name and the title of the Item, after sucessfull PUT request:
    @Test
    public void updateItem_input_valid(){
        //given
        userRepository.save(user);
        tagsRepository.save(tag);

        Item item2 = new Item();
        item2.setUserId(user.getId());
        item2.setDescription("Description2");
        item2.setTitle("Title2");
        List<Tags> tags = new ArrayList<>();
        tags.add(tag);
        item2.setItemtags(tags);

        Item itemUpdated = itemService.updateItem(item, item2);

        assertEquals(itemUpdated.getTitle(),item2.getTitle());
        assertEquals(itemUpdated.getDescription(),item2.getDescription());
    }
    // We check here, if we receive the correct Message
    @Test
    public void itemUpdateReportCount_NotDeleted(){
        //given
        assertNull(itemRepository.findById(1));
        // We create an Item with 1 Report
        // Creating the Items
        userRepository.save(user);
        tagsRepository.save(tag);

        Item item = new Item();
        item.setUserId(user.getId());
        item.setDescription("Test Description");
        item.setTitle("Title");
        item.setReportcount(1);
        itemRepository.save(item);

        String message = itemService.updateReportCount(item.getId());
        assertEquals(message,"The reportcount has been increased by 1!");
    }
    // In this test we check, if the item also gets deleted, after having
    // count == 3
    @Test
    public void itemGetsDeleted_Count3(){
        //given
        assertTrue(itemRepository.findAll().isEmpty());

        User savedUser = userRepository.save(user);
        tagsRepository.save(tag);
        item.setUserId(savedUser.getId());

        // We create an Item with 1 Report
        // Creating the Items

        item.setReportcount(2);
        Item repoItem = itemRepository.save(item);

        String message = itemService.updateReportCount(repoItem.getId());
        // We check, that the item is also deleted
        assertEquals(message,"The item had 3 reports and was deleted!");
        assertTrue(itemRepository.findById(item.getId()).isEmpty());
    }

}
