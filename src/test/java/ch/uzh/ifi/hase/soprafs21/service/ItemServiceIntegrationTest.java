package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.Item;
import ch.uzh.ifi.hase.soprafs21.entity.Tags;
import ch.uzh.ifi.hase.soprafs21.repository.ItemRepository;
import ch.uzh.ifi.hase.soprafs21.repository.TagsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;


import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WebAppConfiguration
@SpringBootTest
public class ItemServiceIntegrationTest {

    @Qualifier("itemRepository")
    @Autowired
    private ItemRepository itemRepository;

    @Qualifier("tagsRepository")
    @Autowired
    private TagsRepository tagsRepository;

    @Autowired
    private ItemService itemService;

    @Autowired
    private TagsService tagsService;

    @BeforeEach
    public void setup() {
        itemRepository.deleteAll();
        tagsRepository.deleteAll();
    }
    /*
     * Checks the creates a Item funtion
     */
    @Test
    public void createItem_validInputs_success(){
        //given
        assertNull(itemRepository.findById(1));

        // Creating a Tag for the Item
        Tags tag = new Tags();
        tag.setDescription("Test");
        tagsRepository.save(tag);
        List<Tags> tags = new ArrayList<>();
        tags.add(tag);

        // Creating the Item
        Item item = new Item();
        item.setUserId(1L);
        item.setDescription("Test Description");
        item.setTitle("Title");
        item.setItemtags(tags);

        Item itemcreated = itemService.createItem(item);

        assertEquals(item.getId(), itemcreated.getId());
        assertEquals(item.getTitle(), itemcreated.getTitle());
        assertEquals(item.getItemtags(),itemcreated.getItemtags());

    }
    // Checks the function getItemById() throws an error
    @Test
    public void getItembyWrongId_ThrowsError(){
        // Given
        assertNull(itemRepository.findById(1));
        // Creating a Tag for the Item
        Tags tag = new Tags();
        tag.setDescription("Test");
        tagsRepository.save(tag);
        List<Tags> tags = new ArrayList<>();
        tags.add(tag);

        // Creating the Item
        Item item = new Item();
        item.setId(1L);
        item.setUserId(1L);
        item.setDescription("Test Description");
        item.setTitle("Title");
        item.setItemtags(tags);
        itemRepository.save(item);

        // Create Input ID, that does not exist -> 10L
        assertThrows(ResponseStatusException.class, () -> itemService.getItemById(10L));

    }
    // Sucessfully get back the Item with correct ID
    @Test
    public void getItemById_CorrectID(){
        //given
        assertNull(itemRepository.findById(1));

        // Creating the Item
        Item item = new Item();
        item.setUserId(1L);
        item.setDescription("Test Description");
        item.setTitle("Title");
        itemRepository.save(item);

        Item itemtocompare = itemService.getItemById(item.getId());

        assertEquals(item.getId(),itemtocompare.getId());
        assertEquals(item.getTitle(),itemtocompare.getTitle());
        assertEquals(item.getDescription(),itemtocompare.getDescription());
    }
    // Testing the deletion of an Item, that throws an error
    @Test
    public void deleteItemById_throwsError(){
        //given
        assertNull(itemRepository.findById(1));

        // Creating the Item
        Item item = new Item();
        item.setUserId(1L);
        item.setDescription("Test Description");
        item.setTitle("Title");
        itemRepository.save(item);

        // Pass in the wrong ID
        assertThrows(ResponseStatusException.class, () -> itemService.deleteItem(10L));

    }

    // In this test, it is checked, if the deletion of an item works
    @Test
    public void deleteById_success(){
        //given
        assertNull(itemRepository.findById(1));

        // Creating the Item
        Item item = new Item();
        item.setUserId(1L);
        item.setDescription("Test Description");
        item.setTitle("Title");
        itemRepository.save(item);

        // We now delete the item
        itemService.deleteItem(item.getId());

        // Assert, that there no more items inside the database
        assertEquals(itemRepository.count(),0);
    }
    // Testing if we receive all items from
    @Test
    public void returnAllItems(){
        //given
        assertNull(itemRepository.findById(1));

        // Creating the Items
        Item item = new Item();
        item.setUserId(1L);
        item.setDescription("Test Description");
        item.setTitle("Title");
        itemRepository.save(item);

        Item item2 = new Item();
        item2.setUserId(2L);
        item2.setDescription("Test Description2");
        item2.setTitle("Title2");
        itemRepository.save(item);
        List<Item> items = new ArrayList<>();
        items = itemService.getAllItems();

        assert(items.isEmpty()==false);
    }
    // Throws an error, if the userInput has empty Description
    @Test
    public void updateItem_invalid_withNoDescription() {
        //given
        assertNull(itemRepository.findById(1));

        // Creating the Items
        Item item = new Item();
        item.setUserId(1L);
        item.setDescription("Test Description");
        item.setTitle("Title");
        itemRepository.save(item);

        Item item2 = new Item();
        item2.setUserId(2L);
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
        Item item = new Item();
        item.setUserId(1L);
        item.setDescription("Test Description");
        item.setTitle("Title");
        itemRepository.save(item);


        Item item2 = new Item();
        item2.setUserId(2L);
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
        assertNull(itemRepository.findById(1));

        // Creating the Items
        Item item = new Item();
        item.setUserId(1L);
        item.setDescription("Test Description");
        item.setTitle("Title");
        itemRepository.save(item);

        Item item2 = new Item();
        item2.setUserId(2L);
        item2.setDescription("Description2");
        item2.setTitle("Title2");

        Item Itemupdated = itemService.updateItem(item,item2);

        assertEquals(item.getId(), Itemupdated.getId());
        assertEquals(item.getTitle(),item2.getTitle());
        assertEquals(item.getDescription(),item2.getDescription());

    }
    // We check here, if we receive the correct Message
    @Test
    public void itemUpdateReportCount_NotDeleted(){
        //given
        assertNull(itemRepository.findById(1));
        // We create an Item with 1 Report
        // Creating the Items
        Item item = new Item();
        item.setUserId(1L);
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
        assertNull(itemRepository.findById(1));
        // We create an Item with 1 Report
        // Creating the Items
        Item item = new Item();
        item.setUserId(1L);
        item.setDescription("Test Description");
        item.setTitle("Title");
        item.setReportcount(2);
        itemRepository.save(item);

        String message = itemService.updateReportCount(item.getId());
        // We check, that the item is also deleted
        assertEquals(message,"The item had 3 reports and was deleted!");
        assert(itemRepository.count()==0);
    }
    // Get a List of Items based on the Tags, that are in those items
    @Test
    @Transactional
    public void getItems_fromTags(){
        // When
        assertNull(itemRepository.findById(1));
        assertNull(itemRepository.findById(2));

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
        item1.setId(1L);
        item1.setUserId(1L);
        item1.setDescription("Test Description1");
        item1.setTitle("Title");
        List<Tags> tags1 = new ArrayList<>();
        tags1.add(tag1);
        tags1.add(tag2);
        item1.setItemtags(tags1);
        itemRepository.save(item1);

        // Item 2
        Item item2 = new Item();
        item2.setId(2L);
        item2.setUserId(2L);
        item2.setDescription("Test Description2");
        item2.setTitle("Title2");
        List<Tags> tags2 = new ArrayList<>();
        tags2.add(tag1);
        item2.setItemtags(tags2);
        itemRepository.save(item2);

        // Item 3
        Item item3 = new Item();
        item3.setId(3L);
        item3.setUserId(3L);
        item3.setDescription("Test Description3");
        item3.setTitle("Title3");
        List<Tags> tags3 = new ArrayList<>();
        tags3.add(tag3);
        item3.setItemtags(tags3);
        itemRepository.save(item3);

        //List of Tags
        List<String> tags = new ArrayList<>();
        tags.add(tag1.getDescription());
        List<Item> items = itemService.getItemByTagName(tags,3L);

        assert(!items.isEmpty());
        assertNotEquals(items.get(0).getDescription(),"Test Description3");
        assertNotEquals(items.get(1).getDescription(),"Test Description3");
        assert(items.size()==2);
    }





}
