package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.Item;
import ch.uzh.ifi.hase.soprafs21.entity.Matches;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.*;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ch.uzh.ifi.hase.soprafs21.service.ItemService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ItemController {

    // Creates an itemService instance
    @Autowired
    private  ItemService itemService;


    // Post Mapping for creating an Item
    @PostMapping("/users/{userID}/items")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void createItem(@PathVariable("userID")long userId, @RequestBody ItemPostDTO itemPostDTO) {
        itemPostDTO.setUserId(userId);
        // convert API user to internal representation
        Item newItem = DTOMapper.INSTANCE.convertItemPostDTOtoEntity(itemPostDTO);
        // Saves the item in the Database
        itemService.createItem(newItem);
    }

    // Get Mapping to get all items in a list
    @GetMapping("/items")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ItemGetDTO> returnAllItems(){
        // Getting all Item Entities from the database
        List<Item> items = itemService.getAllItems();
        List <ItemGetDTO> itemGetDTOS = new ArrayList<>();

        // Internal representation to API representation
        for(Item item: items){
            itemGetDTOS.add(DTOMapper.INSTANCE.convertEntityToItemGetDTO(item));
        }
        return itemGetDTOS;
    }

    // Get Mapping to get an Item by ID
    @GetMapping("/items/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ItemGetDTO returnItemById(@PathVariable("itemId") long itemId){
        Item item = this.itemService.getItemById(itemId);
        ItemGetDTO itemGetDTO = DTOMapper.INSTANCE.convertEntityToItemGetDTO(item);
        return itemGetDTO;
    }

    // Put Mapping for Updating an Item:
    @PutMapping("/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateItem(@PathVariable("itemId") long itemId, @RequestBody ItemPutDTO itemPutDTO){
        Item currentItem = this.itemService.getItemById(itemId);
        Item inputItem = DTOMapper.INSTANCE.convertItemPutDTOtoEntity(itemPutDTO);
        this.itemService.updateItem(currentItem,inputItem);
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