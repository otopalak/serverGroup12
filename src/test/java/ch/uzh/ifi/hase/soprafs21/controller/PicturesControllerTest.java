package ch.uzh.ifi.hase.soprafs21.controller;
import ch.uzh.ifi.hase.soprafs21.entity.Pictures;
import ch.uzh.ifi.hase.soprafs21.service.ItemService;
import ch.uzh.ifi.hase.soprafs21.service.PictureStorageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(PictureController.class)
class PicturesControllerTest {

    // This allows us to call particular requests on specific paths
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private PictureStorageService pictureStorageService;


    // This test checks, if a picture can get uploaded
    @Test
    public void givenItemId_upload_picture() throws Exception {

        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "image.png",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        MockMvc mockMvc
                = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(multipart("/items/1/pictures/upload").file(file))
                .andExpect(status().isOk());
    }

    // This test checks, if we receive all pictures correctly
    @Test
    public void getPictures_fromItems() throws Exception{
        // Create first Picture
        Pictures picture = new Pictures();
        picture.setItemId(1L);
        picture.setName("picture1");
        picture.setUrl("Google.ch");
        picture.setType("JPEG");

        Pictures picture2 = new Pictures();
        picture2.setItemId(1L);
        picture2.setName("picture2");
        picture2.setUrl("Yahoo.com");
        picture2.setType("PNG");

        List<Pictures> pictures = Arrays.asList(picture,picture2);
        given(pictureStorageService.getAllPictures(1L)).willReturn(pictures);

        // when
        MockHttpServletRequestBuilder getRequest = get("/items/1/pictures/download").contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(picture.getName())))
                .andExpect(jsonPath("$[0].url", is(picture.getUrl())))
                .andExpect(jsonPath("$[1].name", is(picture2.getName())))
                .andExpect(jsonPath("$[1].url", is(picture2.getUrl())));
    }

    // Checks the delete mapping
    @Test
    public void deleteMappingWorks() throws Exception{
        mockMvc.perform(delete("/pictures/delete/pictureName").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

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