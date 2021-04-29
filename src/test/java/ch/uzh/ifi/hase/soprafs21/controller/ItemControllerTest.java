package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.Item;
import ch.uzh.ifi.hase.soprafs21.entity.Tags;
import ch.uzh.ifi.hase.soprafs21.rest.dto.ItemPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.ItemPutDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.ReportMessageDTO;
import ch.uzh.ifi.hase.soprafs21.service.ItemService;
import ch.uzh.ifi.hase.soprafs21.service.TagsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
public class ItemControllerTest {

    // Allows us to call particular requests on specific paths
    @Autowired
    private MockMvc mockMvc;

    // Dummy Mock Service -> No need to implement persistence
    @MockBean
    private ItemService itemService;

    @MockBean
    private TagsService tagsService;

    /*
     * This test is for the GET Request (/items), to get all items. We check what happens, when one Item is created,
     * if we receive it back in a GET Request.
     */
    @Test
    public void givenItem_whenGetItems_thenReturnJsonArray() throws Exception {
        // Given
        // We first create Tags and give them to our Item
        Tags tag1 = new Tags();
        Tags tag2 = new Tags();
        tag1.setDescription("TestTag1");
        tag2.setDescription("TestTag2");
        List<Tags> tags = new ArrayList<>();
        tags.add(tag1);
        tags.add(tag2);

        // We create an Item with Description, Title and The tags
        Item item = new Item();
        item.setTitle("Test Title");
        item.setDescription("Test Description");
        item.setItemtags(tags);


        // This is what we are expecting to have returned
        List<Item> allItems = Collections.singletonList(item);

        // This now mocks the Itemservice -> We define, what getAllItems should return
        given(itemService.getAllItems()).willReturn(allItems);

        //when
        MockHttpServletRequestBuilder getRequest = get("/items").contentType(MediaType.APPLICATION_JSON);

        // then we perform the action
        // Specifically for the ItemTags:
        List<Tags> tagsfromEntity = item.getItemtags();
        List<String> stringTags = new ArrayList<>();
        for (Tags tag : tagsfromEntity) {
            stringTags.add(tag.getDescription());
        }
        System.out.println(item.getItemtags().toString());
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(item.getTitle())))
                .andExpect(jsonPath("$[0].description", is(item.getDescription())))
                .andExpect(jsonPath("$[0].tagsItem", is(stringTags)));


    }

    /*
     * This test is for the GET Request (/items), to get all items. We check what happens, when two Item are created,
     * if we receive them both back in a GET Request.
     */
    @Test
    public void givenItems_whenGetItems_thenReturnJsonArray() throws Exception {
        // Given -> This test will not add any Tags to our Item -> []

        // Item 1
        Item item1 = new Item();
        item1.setTitle("Test Title");
        item1.setDescription("Test Description");

        // Item 2
        Item item2 = new Item();
        item2.setTitle("Test Title2");
        item2.setDescription("Test Description2");


        // This is what we are expecting to have returned
        List<Item> allItems = Arrays.asList(item1, item2);

        // This now mocks the Itemservice -> We define, what getAllItems should return
        given(itemService.getAllItems()).willReturn(allItems);

        //when
        MockHttpServletRequestBuilder getRequest = get("/items").contentType(MediaType.APPLICATION_JSON);

        // then we perform the action
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is(item1.getTitle())))
                .andExpect(jsonPath("$[0].description", is(item1.getDescription())))
                .andExpect(jsonPath("$[0].tagsItem", is(item1.getItemtags())))
                .andExpect(jsonPath("$[1].title", is(item2.getTitle())))
                .andExpect(jsonPath("$[1].description", is(item2.getDescription())))
                .andExpect(jsonPath("$[1].tagsItem", is(item1.getItemtags())));

    }

    /*
     * In this Method, we will Test the POST Method for the Item.
     * We should receive an Item back after the Post method is done.
     */
    @Test
    public void createItem_validInput_itemCreated() throws Exception {
        // Given
        // Creates first the Tag
        Tags tag = new Tags();
        tag.setDescription("Test Tag Description");
        List<Tags> tags = new ArrayList<>();
        tags.add(tag);

        // Creates the Item:
        Item item = new Item();
        item.setId(1L);
        item.setUserId(1L);
        item.setDescription("Test Item");
        item.setTitle("Test Title");
        item.setItemtags(tags);


        //We create the ItemPostDTO
        ItemPostDTO itemPostDTO = new ItemPostDTO();
        itemPostDTO.setTitle("Test Title");
        itemPostDTO.setUserId(1L);
        itemPostDTO.setDescription("Test Item");
        List<String> tagsforDTO = new ArrayList<>();
        tagsforDTO.add("Test Tag Description");
        itemPostDTO.setTagsItem(tagsforDTO);

        // Arrange -> Whatever user we enter we will receive item
        given(itemService.createItem(Mockito.any())).willReturn(item);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users/1/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(itemPostDTO));

        // then (Act)

        // This function is needed to map the tags into the item
        List<String> tagsReturn = new ArrayList<>();
        item.getItemtags().forEach(element -> tagsReturn.add(element.getDescription()));
        mockMvc.perform(postRequest)
                // Assert expected outcomes
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description", is(item.getDescription())))
                .andExpect(jsonPath("$.title", is(item.getTitle())))
                .andExpect(jsonPath("$.userId", is(item.getUserId().intValue())))
                .andExpect(jsonPath("$.tagsItem", is(tagsReturn)));

    }
    /*
     * Testing PUT Request to /items/1/
     */

    @Test
    public void updateItem_valid() throws Exception {
        Item item = new Item();
        item.setUserId(1L);
        item.setId(1L);
        item.setTitle("Test Title");
        item.setDescription("Test Description");

        ItemPutDTO itemPutDTO = new ItemPutDTO();
        itemPutDTO.setTitle("New Title");
        itemPutDTO.setDescription("New Description");

        given(itemService.updateItem(Mockito.any(), Mockito.any())).willReturn(item);

        MockHttpServletRequestBuilder putRequest = put("/items/1/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(itemPutDTO));

        // then -> Returns NO_CONTENT SO CHECK STATUS
        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());

    }

    /*
     * This Test checks the GET Request to /items/{itemId}. It checks, if the user can get the item by the ID
     */
    @Test
    public void givenItem_whenGetItemById_thenReturnJsonArray() throws Exception {
        Item item = new Item();
        item.setId(1L);
        item.setDescription("Test Description");
        item.setTitle("Test Title");

        // Mocks the itemservice
        given(itemService.getItemById(item.getId())).willReturn(item);

        MockHttpServletRequestBuilder getRequest = get("/items/1").contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item.getId().intValue())))
                .andExpect(jsonPath("$.description", is(item.getDescription())))
                .andExpect(jsonPath("$.title", is(item.getTitle())));

    }

    /*
     * This is a Test for the GET Mapping, that we only receive the Items of the specific UserID's
     * GET /users/userID/items
     */
    @Test
    public void givenItems_whenGetItemByUserID_thenReturnJsonArray() throws Exception {
        // Items belonging to User with ID 1
        Item firstitem = new Item();
        firstitem.setId(1L);
        firstitem.setUserId(1L);
        firstitem.setDescription("Description1");
        firstitem.setTitle("Title1");

        Item seconditem = new Item();
        seconditem.setId(2L);
        seconditem.setUserId(1L);
        seconditem.setDescription("Description2");
        seconditem.setTitle("Title2");

        //Item belonging to User with ID = 2
        Item thirditem = new Item();
        thirditem.setId(3L);
        thirditem.setUserId(2L);
        thirditem.setDescription("Description1");
        thirditem.setTitle("Title1");

        // This is what we are expecting to have returned
        List<Item> allItems = Arrays.asList(firstitem, seconditem);

        // This now mocks the Itemservice -> We define, what getAllItems should return
        given(itemService.getAllItemsbyUserId(firstitem.getUserId())).willReturn(allItems);

        //when
        MockHttpServletRequestBuilder getRequest = get("/users/1/items/").contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is(firstitem.getTitle())))
                .andExpect(jsonPath("$[0].description", is(firstitem.getDescription())))
                .andExpect(jsonPath("$[0].tagsItem", is(firstitem.getItemtags())))
                .andExpect(jsonPath("$[1].title", is(seconditem.getTitle())))
                .andExpect(jsonPath("$[1].description", is(seconditem.getDescription())))
                .andExpect(jsonPath("$[1].tagsItem", is(seconditem.getItemtags())));

    }

    /*
     * This Test covers the delete Mapping /items/{itemId}
     */
    @Test
    public void givenItemId_deleteItemById() throws Exception {
        Item item = new Item();
        item.setId(1L);
        item.setUserId(1L);
        item.setDescription("Description");
        item.setTitle("Title");


        Mockito.when(itemService.deleteItem(item.getId())).thenReturn("SUCCESS");

        this.mockMvc.perform(MockMvcRequestBuilders
                .delete("/items/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    /*
     * Tests the increase in Reports in the POST Mapping to "/items/{itemId}/report"
     */
    @Test
    public void givenItem_increaseReportCount() throws Exception {
        Item item = new Item();
        item.setId(1L);
        item.setReportcount(0);
        item.setTitle("Title");
        item.setDescription("Description");
        String message = "The reportcount has been increased by 1!";
        ReportMessageDTO reportMessageDTO = new ReportMessageDTO();

        given(itemService.updateReportCount(item.getId())).willReturn(message);

        MockHttpServletRequestBuilder postRequest = post("/items/1/report")
                .contentType(MediaType.APPLICATION_JSON);


        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(message)));

    }


    /**
     * Helper Method to convert userPostDTO into a JSON string such that the input can be processed
     * Input will look like this: {"name": "Test User", "username": "testUsername"}
     *
     * @param object
     * @return string
     */
    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("The request body could not be created.%s", e.toString()));
        }
    }
}



