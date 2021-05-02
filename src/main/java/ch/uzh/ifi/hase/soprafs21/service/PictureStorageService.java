package ch.uzh.ifi.hase.soprafs21.service;

import java.io.IOException;
import java.util.*;

import ch.uzh.ifi.hase.soprafs21.bucket.BucketName;
import ch.uzh.ifi.hase.soprafs21.entity.Item;
import ch.uzh.ifi.hase.soprafs21.entity.Pictures;
import ch.uzh.ifi.hase.soprafs21.repository.ItemRepository;
import ch.uzh.ifi.hase.soprafs21.repository.PictureDBRepository;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

@Service
@Transactional
public class PictureStorageService {

    @Autowired
    private  FileStore fileStore;

    private final PictureDBRepository pictureDBRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public PictureStorageService(@Qualifier("pictureDBRepository") PictureDBRepository pictureDBRepository, @Qualifier("itemRepository")ItemRepository itemRepository) {
        this.pictureDBRepository = pictureDBRepository;
        this.itemRepository = itemRepository;
    }

    /*
     * Retrive all pictures
     */
    public List<Pictures> getAllPictures(long itemId) {
        List<Pictures> pictures = pictureDBRepository.findAllByItemId(itemId);
        return pictures;
    }

    // Delete Picture from AWS 3 and delete Picture Item
    public String deleteImage(String pictureName){
        String bucketname = BucketName.Item_Image.getBucketName();
        Pictures picture = pictureDBRepository.findByName(pictureName);
        String key = picture.getItemId() + "/" + pictureName;
        String response = fileStore.delete(bucketname,key);
        if(response=="Delete Successfully"){
            long itemId = picture.getItemId();
            Item item = itemRepository.findById(itemId);
            item.setPicturecount(item.getPicturecount()-1);
            pictureDBRepository.delete(picture);
            return response;
        }else {
            return response;
        }
    }
    // Delete all pictures of an Item out of the database and out of the S3-Bucket
    // References the deleteImage function
    public String deleteAllPicturesById(long id){
        try{
            List<Pictures> pictures = pictureDBRepository.findAllByItemId(id);
            for(Pictures picture:pictures){
                this.deleteImage(picture.getName());
            }
            pictureDBRepository.deleteAllByItemId(id);
    }catch(IllegalStateException e){
            return "Cannot Delete Pictures!";
        }
        return "Delete Successfull!";
    }

    /*
     * This function is for uploading an image to the S3 Server and creating an Entity pictures
     * In S3 each item id has 1 Folder with all pictures in it!
     */
    public void uploadItemImage(long id, MultipartFile file) throws IOException {
        // 1. Check if image is empty
        if (file.isEmpty()) {
            String baseErrorMessage = "You need to upload an image!";
            throw new ResponseStatusException(HttpStatus.CONFLICT,String.format(baseErrorMessage));
        }
        // 2. Check if the file is an image (We allow JPEG,PNG and GIF's)
        if (!Arrays.asList(ContentType.IMAGE_JPEG.getMimeType(), ContentType.IMAGE_PNG.getMimeType(), ContentType.IMAGE_GIF.getMimeType()).contains(file.getContentType())) {
            String baseErrorMessage = "Your filetype is not a JPEG,PNG or a GIF";
            throw new ResponseStatusException(HttpStatus.CONFLICT,String.format(baseErrorMessage));
        }
        // 3. Does the corresponding item already exist?
        Item thisitem = this.itemRepository.findById(id);
        if (thisitem == null) {
            String baseErrorMessage = "The item does not exist, thus you cannot add pictues!";
            throw new ResponseStatusException(HttpStatus.CONFLICT,String.format(baseErrorMessage));
        }

        // 4. Checks if the image counter of the item < 5:
        if(thisitem.getPicturecount()==5){
            String baseErrorMessage = "You cannot upload more than 5 pictures!";
            throw new ResponseStatusException(HttpStatus.CONFLICT,String.format(baseErrorMessage));
        }
        // 5. Create a new Picture Entity + Increase the counter by 1 of uploaded pictures
        UUID uuid = UUID.randomUUID();
        String URL = "https://mypicturegallerydshush.s3.eu-central-1.amazonaws.com/" + thisitem.getId().toString() + "/" + uuid.toString() + "-" + file.getOriginalFilename();
        String name = uuid + "-" + file.getOriginalFilename();
        Pictures picture = new Pictures(thisitem.getId(),name,file.getContentType(),URL);
        pictureDBRepository.save(picture);
        thisitem.setPicturecount(thisitem.getPicturecount()+1);

        // 5. Grab some metadata from the file
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Lenght", String.valueOf(file.getSize()));

        //5. Store to S3 Bucket:
        // Creates a folder for itemId
        String path = String.format("%s/%s", BucketName.Item_Image.getBucketName(), thisitem.getId());
        String filename =  uuid.toString()+ "-" + file.getOriginalFilename();
        if(System.getenv("AWS_ACCESS_KEY_ID") != null ){
            fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
        }
    }

}