package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.Pictures;
import ch.uzh.ifi.hase.soprafs21.rest.dto.PictureGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.ItemService;
import ch.uzh.ifi.hase.soprafs21.service.PictureStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Needed for the URI, that is sent back
@RestController
public class PictureController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private PictureStorageService pictureStorageService;

    // Post a picture
    @PostMapping(
            path = "/items/{itemId}/pictures/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String uploadProfileImage(@PathVariable("itemId")long itemId,
                                   @RequestParam("file") MultipartFile file) throws IOException {
       String message = pictureStorageService.uploadItemImage(itemId, file);
       return message;
    }

    // Retrieve all pictures
    @GetMapping("/items/{itemId}/pictures/download")
    @ResponseStatus(HttpStatus.OK)
    public List<PictureGetDTO> downloadImage(@PathVariable("itemId")long itemId){
        List<Pictures> pictures = pictureStorageService.getAllPictures(itemId);
        List<PictureGetDTO> pictureGetDTOS = new ArrayList<>();

        for (Pictures picture:pictures){
            pictureGetDTOS.add(DTOMapper.INSTANCE.convertEntityToPictureGetDTO(picture));
        }
        return pictureGetDTOS;
    }

    // Delete a File
    @DeleteMapping("/pictures/delete/{pictureName}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String deletePicture(@PathVariable("pictureName")String pictureName){
        String response = pictureStorageService.deleteImage(pictureName);
        return response;
    }

}



