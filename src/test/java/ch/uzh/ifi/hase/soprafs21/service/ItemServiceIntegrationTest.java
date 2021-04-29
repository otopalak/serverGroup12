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











}
