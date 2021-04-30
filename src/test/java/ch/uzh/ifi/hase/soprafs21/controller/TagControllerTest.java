package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.Tags;
import ch.uzh.ifi.hase.soprafs21.rest.dto.TagPostDTO;
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
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TagController.class)
public class TagControllerTest {

    // This allows us to call particular requests on specific paths
    @Autowired
    private MockMvc mockMvc;

    // Dummy Mock Service -> No need to implement the persistance
    @MockBean
    private TagsService tagsService;

    /*
     * This test, checks the get all tags GET /tags method
     */
    @Test
    public void givenTag_whenGetTags_thenGetTags()throws Exception{
        Tags tag1 = new Tags();
        Tags tag2 = new Tags();
        tag1.setDescription("Tag 1");
        tag2.setDescription("Tag 2");

        // Create an array with the first and second tag
        List<Tags> allTags = Arrays.asList(tag1,tag2);

        // this mocks the tagService -> should return allTags
        given(tagsService.getAllTags()).willReturn(allTags);

        //when
        MockHttpServletRequestBuilder getRequest = get("/tags").contentType(MediaType.APPLICATION_JSON);

        //then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].description", is(tag1.getDescription())))
                .andExpect(jsonPath("$[1].description", is(tag2.getDescription())));

    }
    /*
     * This Test is for checking out, if we can add a Tag to our Repository
     */

    @Test
    public void createTag_validInput_TagCreated()throws Exception{
        // given
        Tags tag = new Tags();
        tag.setId(1L);
        tag.setDescription("Tag 1");

        // We then create a TAGPOST DTO
        TagPostDTO tagPostDTO = new TagPostDTO();
        tagPostDTO.setDescription("Tag 1");

        // given
        given(tagsService.createTag(Mockito.any())).willReturn(tag);

        // When / Then -> DO the request + validate the rest
        MockHttpServletRequestBuilder postRequest = post("/tag")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(tagPostDTO));

        // then Act
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description", is(tag.getDescription())));


    }










    /**
     * Helper Method to convert userPostDTO into a JSON string such that the input can be processed
     * Input will look like this: {"name": "Test User", "username": "testUsername"}
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
