package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.Pictures;
import ch.uzh.ifi.hase.soprafs21.rest.dto.PictureGetDTO;
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
            path = "/items/{itemId}/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public void uploadProfileImage(@PathVariable("itemId")long itemId,
                                   @RequestParam("file") MultipartFile file) throws IOException {
        pictureStorageService.uploadItemImage(itemId, file);
    }

    // Retrieve all pictures
    @GetMapping("/items/{itemId}/download")
    @ResponseStatus(HttpStatus.OK)
    public List<String> downloadImage(@PathVariable("itemId")long itemId){
        List<Pictures> pictures = pictureStorageService.getAllPictures(itemId);

        List<String> names = new ArrayList<>();
        for (Pictures picture:pictures){
            names.add(picture.getName());
            System.out.println(picture.getName());
            names.add(picture.getUrl());
            System.out.println(picture.getUrl());

        }
        return names;
    }

}



