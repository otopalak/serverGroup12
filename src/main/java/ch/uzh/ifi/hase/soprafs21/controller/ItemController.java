package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.Item;
import ch.uzh.ifi.hase.soprafs21.entity.Matches;
import ch.uzh.ifi.hase.soprafs21.entity.Tags;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.*;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.TagsService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import com.amazonaws.services.s3.AmazonS3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ch.uzh.ifi.hase.soprafs21.service.ItemService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.print.attribute.standard.Media;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ItemController {

    // Creates an itemService instance
    private  ItemService itemService;
    private  TagsService tagsService;

    @Autowired
    public ItemController(ItemService itemService,TagsService tagsService){
        this.tagsService = tagsService;
        this.itemService = itemService;
    }


    // Post Mapping for creating an Item
    @PostMapping("/users/{userID}/items")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void createItem(@PathVariable("userID")long userId, @RequestBody ItemPostDTO itemPostDTO) {
        itemPostDTO.setUserId(userId);
        // Given a String List of Tags, we add them to the item
        List<String> tagsString = itemPostDTO.getTagsItem();
        List<Tags> tagsTags = new ArrayList<>();
        for(String tag:tagsString){
            tagsTags.add(this.tagsService.getTagByDescription(tag));
        }
        // convert API user to internal representation
        Item newItem = DTOMapper.INSTANCE.convertItemPostDTOtoEntity(itemPostDTO);
        newItem.setItemtags(tagsTags);
        // Saves the item in the Database
        itemService.createItem(newItem);
    }
    @GetMapping("/users/{userID}/items")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ItemGetDTO> returnAllItemsbyUserId(@PathVariable("userID")long userId){
        List<Item> items = itemService.getAllItemsbyUserId(userId);
        List <ItemGetDTO> itemGetDTOS1 = new ArrayList<>();
        // Internal representation to API representation
        for(Item item: items){
            // Adding the tags to the itemGetDTO's
            List<String> tags = new ArrayList<>();
            List<Tags> tagsTags = item.getItemtags();
            ItemGetDTO itemGetDTO = DTOMapper.INSTANCE.convertEntityToItemGetDTO(item);
            for(Tags tag: tagsTags){
                tags.add(tag.getDescription());
            }
            itemGetDTO.setTagsItem(tags);
            itemGetDTOS1.add(itemGetDTO);
        }
        return itemGetDTOS1;
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
            // Adding the tags to the itemGetDTO's
            List<String> tags = new ArrayList<>();
            List<Tags> tagsTags = item.getItemtags();
            ItemGetDTO itemGetDTO = DTOMapper.INSTANCE.convertEntityToItemGetDTO(item);
            for(Tags tag: tagsTags){
                tags.add(tag.getDescription());
            }
            itemGetDTO.setTagsItem(tags);
            itemGetDTOS.add(itemGetDTO);
        }
        return itemGetDTOS;
    }

    // Get Mapping to get an Item by ID
    @GetMapping("/items/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ItemGetDTO returnItemById(@PathVariable("itemId") long itemId){
        Item item = this.itemService.getItemById(itemId);
        // Getting all the tags from the item:
        List<Tags> tags = item.getItemtags();
        List<String> tagsString = new ArrayList<>();
        ItemGetDTO itemGetDTO = DTOMapper.INSTANCE.convertEntityToItemGetDTO(item);
        for(Tags tag:tags){
            tagsString.add(tag.getDescription());
        }
        itemGetDTO.setTagsItem(tagsString);
        return itemGetDTO;
    }

    @GetMapping("/items/{itemId}/proposal")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ItemGetDTO> itemProposal(@PathVariable("itemId") long itemId){
        List<Item> proposal = this.itemService.likeProposals(itemId);
        List <ItemGetDTO> itemGetDTOS = new ArrayList<>();
        // Internal representation to API representation
        for(Item item: proposal){
            ItemGetDTO itemGetDTO = DTOMapper.INSTANCE.convertEntityToItemGetDTO(item);
            itemGetDTOS.add(itemGetDTO);
        }
        return itemGetDTOS;
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
    // creates a match between idOne and idTwo
    @GetMapping("/item/{idOne}/{idTwo}")
    public void findChatMessages (@PathVariable Long idOne,
                                               @PathVariable Long idTwo) {
        itemService.createMatch(idOne, idTwo);
    }
}

