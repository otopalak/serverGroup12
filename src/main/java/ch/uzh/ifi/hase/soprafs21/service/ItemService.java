package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.Item;
import ch.uzh.ifi.hase.soprafs21.entity.Matches;
import ch.uzh.ifi.hase.soprafs21.entity.Tags;
import ch.uzh.ifi.hase.soprafs21.entity.Like;
import ch.uzh.ifi.hase.soprafs21.repository.ItemRepository;
import ch.uzh.ifi.hase.soprafs21.repository.MatchRepository;
import ch.uzh.ifi.hase.soprafs21.repository.LikeRepository;
import ch.uzh.ifi.hase.soprafs21.repository.TagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private PictureStorageService pictureStorageService;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private TagsRepository tagsRepository;


    // Saves the item in the database
    public Item createItem(Item itemToCreate) {
        if(itemToCreate.getDescription().isBlank()||itemToCreate.getTitle().isBlank()|| itemToCreate.getItemtags().isEmpty()){
            String baseErrorMessage = "You need to define a Title a Description and Tags for the Item!";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage));
        }
        itemRepository.save(itemToCreate);
        itemRepository.flush();
        return itemToCreate;
    }

    // Get all items from the database
    public List<Item> getAllItems() {
        return this.itemRepository.findAll();
    }

    // Get Item by ID -> Throws error, if Item with this id not present
    public Item getItemById(long id) {
        Item item = itemRepository.findById(id);
        if (item == null) {
            String baseErrorMessage = "The item with this id does not exist";
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage));
        }
        else {
            return item;
        }
    }
    public List<Item> likeProposals(long myItemId, String searchTag) {
        List<Item> possibleItemsToLike = this.getAllItems();
        List<Item> itemProposal = new ArrayList<>();
        Item myItem = getItemById(myItemId);
        Collections.shuffle(possibleItemsToLike, new Random());
        for(Item item : possibleItemsToLike) {
            Like likedItem = likeRepository.findByItemIDSwipedAndItemIDSwiper(item.getId(), myItemId);
            if(likedItem == null && ((int) (long) item.getUserId()) != ((int) (long) myItem.getUserId())){
                List<Tags> tags = item.getItemtags();
                // get tag
                Tags tag = tagsRepository.getTagsByDescription(searchTag);
                if (tags.contains(tag)){
                    itemProposal.add(item);
                }
                if(tag == null){
                    itemProposal.add(item);
                }
            }
            if(itemProposal.size() > 5) {
                break;
            }
        }
        return  itemProposal;
    }

    //Update the item
    public Item updateItem(Item currentItem, Item userInput) {
        // Changes the Description of the item
        if (!userInput.getDescription().isBlank()) {
            currentItem.setDescription(userInput.getDescription());
        }else{
            String baseErrorMessage = "You need a Description!";
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage));
        }
        // Changes the Title of the item
        if (!userInput.getTitle().isBlank()) {
            currentItem.setTitle(userInput.getTitle());
        }else{
            String baseErrorMessage = "You need a Title!";
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage));
        }
        itemRepository.save(currentItem);
        return currentItem;
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



    // Updates the report count of the Item
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