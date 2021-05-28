package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.Matches;
import ch.uzh.ifi.hase.soprafs21.service.MatchService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MatchController.class)
class MatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MatchService matchService;

    @Test
    void getMatch_validId() throws Exception {
        Matches match = new Matches();
        match.setId((long) 1);
        match.setItemIdOne((long) 1);
        match.setItemIdTwo((long) 2);

        //@GetMapping("/showmatch/{matchID}")

        given(matchService.getMatchByMatchID(match.getId())).willReturn(match);

        MockHttpServletRequestBuilder getRequest = get("/showmatch/1").contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isOk())
                //.andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.id", is(match.getId().intValue())))
                .andExpect(jsonPath("$.itemIdOne", is(match.getItemIdOne().intValue())))
                .andExpect(jsonPath("$.itemIdTwo", is(match.getItemIdTwo().intValue())));
    }
    @Test
    void getMatch_invalidId() throws Exception {
        //@GetMapping("/showmatch/{matchID}")

        given(matchService.getMatchByMatchID(anyLong())).willReturn(null);

        MockHttpServletRequestBuilder getRequest = get("/showmatch/1").contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isNotFound());
    }

    @Test
    void getAllMatchesByItemID_validId() throws Exception {
        List<Matches> matchesRepo = new ArrayList<>();

        Matches match = new Matches();
        match.setId((long) 1);
        match.setItemIdOne((long) 1);
        match.setItemIdTwo((long) 2);

        matchesRepo.add(match);

        given(matchService.getAllMatchesByItemID(anyLong())).willReturn(matchesRepo);

        //@GetMapping("/{itemID}/showmatches")
        MockHttpServletRequestBuilder getRequest = get("/1/showmatches").contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(match.getId().intValue())))
                .andExpect(jsonPath("$[0].itemIdOne", is(match.getItemIdOne().intValue())))
                .andExpect(jsonPath("$[0].itemIdTwo", is(match.getItemIdTwo().intValue())));

    }

    @Test
    void getAllMatchesByItemID_invalidId() throws Exception {
        List<Matches> matchesRepo = new ArrayList<>();

        // returns empty list
        given(matchService.getAllMatchesByItemID(anyLong())).willReturn(matchesRepo);

        //@GetMapping("/{itemID}/showmatches")
        MockHttpServletRequestBuilder getRequest = get("/1/showmatches").contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
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