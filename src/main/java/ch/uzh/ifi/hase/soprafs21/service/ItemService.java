package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.Item;
import ch.uzh.ifi.hase.soprafs21.entity.Matches;
import ch.uzh.ifi.hase.soprafs21.entity.Tags;
import ch.uzh.ifi.hase.soprafs21.repository.ItemRepository;
import ch.uzh.ifi.hase.soprafs21.repository.MatchRepository;
import com.amazonaws.AmazonServiceException;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private PictureStorageService pictureStorageService;


    // Saves the item in the database
    public void createItem(Item itemToCreate) {
        itemRepository.save(itemToCreate);
        itemRepository.flush();
    }

    // Get all items from the database
    public List<Item> getAllItems() {
        return this.itemRepository.findAll();
    }

    // Get Item by ID -> Throws error, if Item with this id not present
    public Item getItemById(long id) {
        Item item = this.itemRepository.findById(id);
        if (item == null) {
            String baseErrorMessage = "The item with this id does not exist";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage));
        }
        else {
            return item;
        }
    }

    //Update the item
    public void updateItem(Item currentItem, Item userInput) {
        // Changes the Description of the item
        if (!userInput.getDescription().isBlank()) {
            currentItem.setDescription(userInput.getDescription());
        }
        // Changes the Title of the item
        if (!userInput.getTitle().isBlank()) {
            currentItem.setTitle(userInput.getTitle());
        }
        itemRepository.save(currentItem);
    }

    // here just temporarely to test the chat feature
    public void createMatch(Long idOne, Long idTwo) {
        Matches newMatch = new Matches();
        newMatch.setItemIdOne(idOne);
        newMatch.setItemIdTwo(idTwo);
        matchRepository.save(newMatch);
    }


    public List<Item> getAllItemsbyUserId(long userId) {
        return itemRepository.findItemsByUserId(userId);
    }

    // This function deletes all items
    public String deleteItem(long id) {
        Item item = itemRepository.findById(id);
        if (item == null) {
            String baseErrorMessage = "The item with this id does not exist";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage));
        }
        else {
            String delete = pictureStorageService.deleteAllPicturesById(id);
            if (delete == "Delete Successfull!") {
                itemRepository.delete(item);
                return "Deletion Successfull";
            }
            else {
                return "Deletion failed!";
            }
        }
    }

    // Returns the items based on the Tag -> ALl Items, that have this tag
    public List<Item> getItemByTagName(List<String> tagNames, long userId) {
        List<Item> items = new ArrayList<>();
        for(String tagInput:tagNames) {
            // We first find all items
            List<Item> itemsToTest = itemRepository.findAll();
            for (Item item : itemsToTest) {
                //We now get The list of all Tags per Item
                List<Tags> tags = item.getItemtags();
                // If the tag name == tag input -> Append to items
                for (Tags tag : tags) {
                    if (tag.getDescription().equals(tagInput) && !items.contains(item) && item.getUserId() != userId) {
                        items.add(item);
                    }
                }

            }
        }
        return items;
    }

    public String updateReportCount(long itemId) {
        Item item = itemRepository.findById(itemId);
        String message = "";
            item.setReportcount(item.getReportcount()+1);
            if(item.getReportcount()<=2) {
                message = "The reportcount has been increased by 1!";
                itemRepository.save(item);
            }else{
                this.deleteItem(itemId);
                message = "The item had 3 reports and was deleted!";
            }
        return message;
    }
}