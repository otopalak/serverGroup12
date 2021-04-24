package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.bucket.BucketName;
import ch.uzh.ifi.hase.soprafs21.entity.Item;
import ch.uzh.ifi.hase.soprafs21.entity.Matches;
import ch.uzh.ifi.hase.soprafs21.entity.Tags;
import ch.uzh.ifi.hase.soprafs21.repository.ItemRepository;
import ch.uzh.ifi.hase.soprafs21.repository.MatchRepository;
import com.amazonaws.AmazonServiceException;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final ItemRepository itemRepository;

    @Autowired
    private final MatchRepository matchRepository;


    public ItemService(ItemRepository itemRepository, MatchRepository matchRepository) {
        this.itemRepository = itemRepository;
        this.matchRepository = matchRepository;
    }

    // Saves the item in the database
    public void createItem(Item itemToCreate){
        itemRepository.save(itemToCreate);
        itemRepository.flush();
    }

    // Get all items from the database
    public List<Item> getAllItems(){
        return this.itemRepository.findAll();
    }

    // Get Item by ID -> Throws error, if Item with this id not present
    public Item getItemById(long id){
        Item item = this.itemRepository.findById(id);
        if(item == null){
            String baseErrorMessage = "The item with this id does not exist";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,String.format(baseErrorMessage));
        }else{
            return item;
        }
    }
    //Update the item
    public void updateItem(Item currentItem,Item userInput){
        // Changes the Description of the item
        if(userInput.getDescription()!=null){
            currentItem.setDescription(userInput.getDescription());
        }
        // Changes the Title of the item
        if(userInput.getTitle()!=null){
            currentItem.setTitle(userInput.getTitle());
        }
    }

    // here just temporarely to test the chat feature
    public void createMatch(Long idOne, Long idTwo){
        Matches newMatch = new Matches();
        newMatch.setItemIdOne(idOne);
        newMatch.setItemIdTwo(idTwo);
        matchRepository.save(newMatch);
    }


}
