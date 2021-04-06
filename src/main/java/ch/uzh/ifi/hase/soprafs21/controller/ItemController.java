package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.Item;
import ch.uzh.ifi.hase.soprafs21.entity.Matches;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.ItemPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.MatchesGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ch.uzh.ifi.hase.soprafs21.service.ItemService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ItemController {
    private final ItemService itemService;

    ItemController(ItemService itemService) {
        this.itemService = itemService;
    }
    @PostMapping("/item")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void createUser(@RequestBody ItemPostDTO itemPostDTO) {
        // Checks if username & password given are not empty

        // convert API user to internal representation
        Item newItem = DTOMapper.INSTANCE.convertItemPostDTOtoEntity(itemPostDTO);

        // create user
        Item createItem = itemService.createItem(newItem);

        // convert internal representation of user back to API
    }

    // To remove only here for testing
    @GetMapping("/item/{idOne}/{idTwo}")
    public void findChatMessages (@PathVariable Long idOne,
                                               @PathVariable Long idTwo) {
        itemService.createMatch(idOne, idTwo);
    }

    //pretty sure wrong endpoint --> change tomorrow
    @GetMapping("/matches/{id}")
    public List<MatchesGetDTO> findMatchesForId (@PathVariable Long id) {
        List<Matches> myMatches = itemService.findMatchesForId(id);
        List<MatchesGetDTO> matchesGetDTOS = new ArrayList<>();

        // convert each user to the API representation
        for (Matches match : myMatches) {
            matchesGetDTOS.add(DTOMapper.INSTANCE.convertEntityToMatchesGetDTO(match));
        }
        return matchesGetDTOS;
    }
}
