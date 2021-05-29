package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.Item;
import ch.uzh.ifi.hase.soprafs21.entity.Tags;
import ch.uzh.ifi.hase.soprafs21.rest.dto.*;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ch.uzh.ifi.hase.soprafs21.service.ItemService;
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
    /*
     * Get Mapping for all items of the user
     */
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
    /*
     * Reports the item (3 reports -> Item gets deleted)
     */

    @PostMapping("/items/{itemId}/report")
    @ResponseStatus(HttpStatus.OK)
    public ReportMessageDTO increaseReportCount(@PathVariable("itemId")long itemId){
        ReportMessageDTO reportMessageDTO = new ReportMessageDTO();
        String message = itemService.updateReportCount(itemId);
        reportMessageDTO.setMessage(message);
        return reportMessageDTO;
    }

    @GetMapping("/items/{itemId}/proposal")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ItemGetDTO> itemProposalNoTag(@PathVariable("itemId") long itemId) {
        return getItems(itemId, "");
    }

    @GetMapping("/items/{itemId}/proposal/{searchTag}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ItemGetDTO> itemProposal(@PathVariable("itemId") long itemId, @PathVariable("searchTag") String searchTag){
        return getItems(itemId, searchTag);
    }

    private List<ItemGetDTO> getItems(long itemId, String searchTag) {
        List<Item> proposal = this.itemService.likeProposals(itemId, searchTag);
        List <ItemGetDTO> itemGetDTOS = new ArrayList<>();
        // Internal representation to API representation
        for(Item item: proposal){
            // Getting all the tags from the item:
            List<Tags> tags = item.getItemtags();
            List<String> tagsString = new ArrayList<>();
            ItemGetDTO itemGetDTO = DTOMapper.INSTANCE.convertEntityToItemGetDTO(item);
            for(Tags tag:tags){
                tagsString.add(tag.getDescription());
            }
            itemGetDTO.setTagsItem(tagsString);
            itemGetDTOS.add(itemGetDTO);
        }
        return itemGetDTOS;
    }

    // Put Mapping for Updating an Item
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
    
    //this return a list containing all itemId's for which the item has been swapped
    @GetMapping("/item/swapHistory/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getSwapHistory(@PathVariable("itemId") long itemId){
        return itemService.getItemById(itemId).getSwapHistory();
    }
}

