package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Item;
import ch.uzh.ifi.hase.soprafs21.entity.Tags;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.ItemRepository;
import ch.uzh.ifi.hase.soprafs21.repository.TagsRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.junit.platform.commons.function.Try;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;


import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
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
    @Disabled
    public void getItemByWrongId_ThrowsError(){
        //assertTrue(itemRepository.findAll().isEmpty());
        // Create Input ID, that does not exist -> 10L
        userRepository.save(user);
        tagsRepository.save(tag);
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
    @Disabled
    public void itemGetsDeleted_Count3(){
        //given
        assertNull(itemRepository.findById(1));

        userRepository.save(user);
        tagsRepository.save(tag);

        // We create an Item with 1 Report
        // Creating the Items

        item.setReportcount(2);
        Item repoItem = itemRepository.save(item);

        String message = itemService.updateReportCount(repoItem.getId());
        // We check, that the item is also deleted
        assertEquals(message,"The item had 3 reports and was deleted!");
        assertEquals(itemRepository.findById(item.getId()), null);
    }
    // Get a List of Items based on the Tags, that are in those items
    @Test
    @Transactional
    @Disabled
    public void getItems_fromTags_empty(){
        // When
        assertNull(itemRepository.findById(1));
        assertNull(itemRepository.findById(2));

        userRepository.save(user);

        // Creating some tags
        Tags tag1 = new Tags();
        tag1.setDescription("tag1");
        tagsRepository.save(tag1);
        Tags tag2 = new Tags();
        tag2.setDescription("tag2");
        tagsRepository.save(tag2);
        Tags tag3 = new Tags();
        tag3.setDescription("tag3");
        tagsRepository.save(tag3);

        // Creating Items

        // Item 1
        Item item1 = new Item();
        item1.setUserId(user.getId());
        item1.setDescription("Test Description1");
        item1.setTitle("Title");
        List<Tags> tags1 = new ArrayList<>();
        tags1.add(tag1);
        tags1.add(tag2);
        item1.setItemtags(tags1);
        itemRepository.save(item1);

        // Item 2
        Item item2 = new Item();
        item2.setUserId(user.getId());
        item2.setDescription("Test Description2");
        item2.setTitle("Title2");
        List<Tags> tags2 = new ArrayList<>();
        tags2.add(tag1);
        item2.setItemtags(tags2);
        itemRepository.save(item2);

        // Item 3
        Item item3 = new Item();
        item3.setUserId(user.getId());
        item3.setDescription("Test Description3");
        item3.setTitle("Title3");
        List<Tags> tags3 = new ArrayList<>();
        tags3.add(tag3);
        item3.setItemtags(tags3);
        itemRepository.save(item3);

        //List of Tags
        List<String> tags = new ArrayList<>();
        tags.add(tag1.getDescription());
        List<Item> items = itemService.getItemByTagName(tags,user.getId());

        assert(items.size()==0);
    }

    @Test
    @Transactional
    @Disabled
    public void getItems_fromTags(){
        // When
        assertNull(itemRepository.findById(1));
        assertNull(itemRepository.findById(2));

        User user2 = new User();
        user2.setId(2L);
        user2.setPassword("password");
        user2.setName("nico");
        user2.setUsername("nico");
        user2.setAddress("address");
        user2.setPostcode(9050);
        user2.setCity("Appenzell");
        user2.setStatus(UserStatus.ONLINE);
        user2.setToken("hello");

        // Creating some tags
        Tags tag1 = new Tags();
        tag1.setDescription("tag1");
        Tags tag2 = new Tags();
        tag2.setDescription("tag2");
        Tags tag3 = new Tags();
        tag3.setDescription("tag3");

        // Creating Items

        // Item 1
        Item item1 = new Item();
        item1.setUserId(user.getId());
        item1.setDescription("Test Description1");
        item1.setTitle("Title");
        List<Tags> tags1 = new ArrayList<>();
        tags1.add(tag1);
        tags1.add(tag2);
        item1.setItemtags(tags1);
        //itemRepository.save(item1);

        // Item 2
        Item item2 = new Item();
        item2.setUserId(user2.getId());
        item2.setDescription("Test Description2");
        item2.setTitle("Title2");
        List<Tags> tags2 = new ArrayList<>();
        tags2.add(tag1);
        item2.setItemtags(tags2);
        //itemRepository.save(item2);

        // Item 3
        Item item3 = new Item();
        item3.setUserId(user2.getId());
        item3.setDescription("Test Description3");
        item3.setTitle("Title3");
        List<Tags> tags3 = new ArrayList<>();
        tags3.add(tag3);
        item3.setItemtags(tags3);
        //itemRepository.save(item3);

        userRepository.save(user);
        userRepository.save(user2);

        tagsRepository.save(tag1);
        tagsRepository.save(tag2);
        tagsRepository.save(tag3);

        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        //List of Tags
        List<String> tags = new ArrayList<>();
        tags.add(tag1.getDescription());
        List<Item> items = itemService.getItemByTagName(tags,user.getId());

        assertTrue(items.size() == 1);
        assertEquals(items.get(0).getId(), item2.getId());
    }
}
