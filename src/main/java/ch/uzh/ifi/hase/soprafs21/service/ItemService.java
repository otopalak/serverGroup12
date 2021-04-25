package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.Item;
import ch.uzh.ifi.hase.soprafs21.entity.Like;
import ch.uzh.ifi.hase.soprafs21.entity.Matches;
import ch.uzh.ifi.hase.soprafs21.repository.ItemRepository;
import ch.uzh.ifi.hase.soprafs21.repository.LikeRepository;
import ch.uzh.ifi.hase.soprafs21.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;


@Service
public class ItemService {

    @Autowired
    private final ItemRepository itemRepository;

    @Autowired
    private final MatchRepository matchRepository;

    @Autowired
    private final LikeRepository likeRepository;

    public ItemService(ItemRepository itemRepository, MatchRepository matchRepository, LikeRepository likeRepository) {
        this.itemRepository = itemRepository;
        this.matchRepository = matchRepository;
        this.likeRepository = likeRepository;
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

    public List<Item> likeProposals(long myItemId) {
        List<Item> possibleItemsToLike = this.getAllItems();
        List<Item> itemProposal = new ArrayList<>();
        for(Item item : possibleItemsToLike) {
            Like likedItem = likeRepository.findByItemIDSwipedAndItemIDSwiper(item.getId(), myItemId);
            if(!(likedItem == null || likedItem.getLiked() == true || likedItem.getLiked() == false)){
                itemProposal.add(item);
            }
            if(itemProposal.size() > 5) {
                break;
            }
        }
        return  itemProposal;
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
