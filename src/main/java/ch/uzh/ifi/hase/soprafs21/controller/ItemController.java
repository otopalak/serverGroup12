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
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public ItemGetDTO createItem(@PathVariable("userID")long userId, @RequestBody ItemPostDTO itemPostDTO) {
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
        Item item = itemService.createItem(newItem);
        // Send it back to the frontend
        List<String> TAGS = new ArrayList<>();
        List<Tags> TAGTAGS = item.getItemtags();
        for(Tags TAG: TAGTAGS){
            TAGS.add(TAG.getDescription());
        }
        ItemGetDTO itemGetDTO = DTOMapper.INSTANCE.convertEntityToItemGetDTO(item);
        itemGetDTO.setTagsItem(TAGS);
        return itemGetDTO;

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
    public ItemGetDTO returnItemById(@PathVariable("itemId") long itemId,@RequestBody ItemGetDTOforVerification itemGetDTOforVerification){
        this.itemService.checkIfItemBelongsToUser(itemId,itemGetDTOforVerification.getUserid());
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

    @PostMapping("/items/{itemId}/report")
    @ResponseStatus(HttpStatus.OK)
    public ReportMessageDTO increaseReportCount(@PathVariable("itemId")long itemId){
        ReportMessageDTO reportMessageDTO = new ReportMessageDTO();
        String message = itemService.updateReportCount(itemId);
        reportMessageDTO.setMessage(message);
        return reportMessageDTO;
    }

    // Put Mapping for Updating an Item:
    @PutMapping("/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateItem(@PathVariable("itemId") long itemId, @RequestBody ItemPutDTO itemPutDTO){
        Item currentItem = this.itemService.getItemById(itemId);
        Item inputItem = DTOMapper.INSTANCE.convertItemPutDTOtoEntity(itemPutDTO);
        this.itemService.updateItem(currentItem,inputItem);
    }

    // This mapping is for deleting an item
    // When deleting an Item also all pictures will be deleted
    @DeleteMapping("/items/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteItem(@PathVariable("itemId") long id){
        String delete = itemService.deleteItem(id);
        return delete;
    }

    // To remove only here for testing
    // creates a match between idOne and idTwo
    @GetMapping("/item/{idOne}/{idTwo}")
    public void findChatMessages (@PathVariable Long idOne,
                                               @PathVariable Long idTwo) {
        itemService.createMatch(idOne, idTwo);
    }
    // Get all items by choosen Tags List
    @GetMapping("users/{userId}/items/tags")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemGetDTO> getItemsByTags(@PathVariable("userId") long userId,@RequestBody TagsGetDTO tagsGetDTO){
        List<Item> items = new ArrayList<>();
        List<String> tags = tagsGetDTO.getTags();
        items = itemService.getItemByTagName(tags,userId);
        // Returning a list of ItemGetDTO's
        List <ItemGetDTO> itemGetDTOS = new ArrayList<>();
        // Internal representation to API representation
        for(Item item: items){
            // Adding the tags to the itemGetDTO's
            List<String> tagsToConvert = new ArrayList<>();
            List<Tags> tagsTags = item.getItemtags();
            ItemGetDTO itemGetDTO = DTOMapper.INSTANCE.convertEntityToItemGetDTO(item);
            for(Tags tagtoAdd: tagsTags){
                tagsToConvert.add(tagtoAdd.getDescription());
            }
            itemGetDTO.setTagsItem(tagsToConvert);
            itemGetDTOS.add(itemGetDTO);
        }
        return itemGetDTOS;

    }
}

