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

    @Autowired
    private  FileStore fileStore;

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

    /// Needed to check out the image upload
    public void uploadProfileImage(long id, MultipartFile file) throws IOException {
        // 1. Check if image is empty
        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file [" + file.getSize() + "]");
        }
        System.out.println("One Step done");
        // 2. Check if file is an image
        if (!Arrays.asList(ContentType.IMAGE_JPEG.getMimeType(), ContentType.IMAGE_PNG.getMimeType(), ContentType.IMAGE_GIF.getMimeType()).contains(file.getContentType())) {
            throw new IllegalStateException("Wrong Content Type! You can only upload Images!");
        }
        System.out.println("Second step done");
        // 3. Does the image exist in the database
        Item thisitem = this.itemRepository.findById(id);
        if (thisitem == null) {
            throw new IllegalStateException("You cannot have no item behind the picture!");
        }

        // 4. Grab some metadata from the file
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Lenght", String.valueOf(file.getSize()));

        //5. Store to S3 Bucket:
        // Creates a folder for itemId
        String path = String.format("%s/%s", BucketName.Item_Image.getBucketName(), thisitem.getId());
        System.out.println(path);
        String filename = file.getOriginalFilename() + "-" + UUID.randomUUID();
        System.out.println(filename);
        thisitem.setPicture(filename);
        this.itemRepository.save(thisitem);

        fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
    }
    // Needed for retrieving the picture
    public byte[] download(long itemId) {
        Item item = this.itemRepository.findById(itemId);
        String path = String.format("%s/%s",BucketName.Item_Image.getBucketName(),item.getId());
        System.out.println("We got here");
        return fileStore.download(path,item.getPicture());
        }
}
