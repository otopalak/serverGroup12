package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.SwapConfirmation;
import ch.uzh.ifi.hase.soprafs21.rest.dto.SwapConfirmationPostDTO;
import ch.uzh.ifi.hase.soprafs21.service.LikeService;
import ch.uzh.ifi.hase.soprafs21.service.SwapConfirmationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SwapConfirmationController.class)
class SwapConfirmationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SwapConfirmationService swapConfirmationService;

    private SwapConfirmation inputSwapConfirmation;
    private SwapConfirmationPostDTO postDTO;

    @BeforeEach
    void setUp() {
        inputSwapConfirmation = new SwapConfirmation();
        inputSwapConfirmation.setItemID1(1L);
        inputSwapConfirmation.setItemID2(2L);

        postDTO = new SwapConfirmationPostDTO();
        postDTO.setItemID1(1L);
        postDTO.setItemID2(2L);
    }

    @Test
    void swap_createSwapConfirmation_valid() throws Exception {

        SwapConfirmation returnedSwapConfirmation = new SwapConfirmation();
        returnedSwapConfirmation.setItemID1(1L);
        returnedSwapConfirmation.setItemID2(2L);
        returnedSwapConfirmation.setItem1ConfirmsItem2(true);

        Mockito.when(swapConfirmationService.createSwapConfirmation(inputSwapConfirmation))
                .thenReturn(returnedSwapConfirmation);

        MockHttpServletRequestBuilder postRequest = post("/swap")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(postDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isNoContent());
    }

    @Test
    void swap_createSwapConfirmation_invalidId1() throws Exception {

        postDTO.setItemID1(null);

        MockHttpServletRequestBuilder postRequest = post("/swap")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(postDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isConflict());
    }

    @Test
    void swap_createSwapConfirmation_invalidId2() throws Exception {
        postDTO.setItemID2(null);

        MockHttpServletRequestBuilder postRequest = post("/swap")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(postDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isConflict());
    }

    @Test
    void cancel() throws Exception {

        Mockito.when(swapConfirmationService.cancelSwapConfirmation(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn("swap canceled");

        MockHttpServletRequestBuilder putRequest = MockMvcRequestBuilders.put("/swap/cancel/1/2")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(putRequest)
                .andExpect(status().isOk());
    }

    @Test
    void checkTrueSwapConfirmation() throws Exception {
        Mockito.when(swapConfirmationService.check(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(true);

        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/swap/check/1/2")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk());
    }

    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("The request body could not be created.%s", e.toString()));
        }
    }
}