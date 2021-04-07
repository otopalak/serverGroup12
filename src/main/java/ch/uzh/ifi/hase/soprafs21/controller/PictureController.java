package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.Item;
import ch.uzh.ifi.hase.soprafs21.entity.Pictures;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.Response.ResponseFile;
import ch.uzh.ifi.hase.soprafs21.rest.Response.ResponseMessage;
import ch.uzh.ifi.hase.soprafs21.rest.dto.PictureGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.PictureStorageService;
import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Needed for the URI, that is sent back
@RestController
public class PictureController {

    @Autowired
    private PictureStorageService pictureStorageService;

    // This mapping is for uploading a File into our Database
    // Creates a Multipartfile and tries to save it into our Database and returns a respond message
    @PostMapping("/upload/{itemId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResponseMessage> uploadFile( @RequestParam("file") MultipartFile file, @PathVariable("itemId") long itemId) {
        String message = "";
        try {
            pictureStorageService.store(file,itemId);
            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    /*
     * This mapping is for getting all pictures from the Database by the itemId
     * This allows us to get all pictures from
     */
    @GetMapping("/pictures/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ResponseFile>> getListofAllPictures(@PathVariable("itemId") long itemId){
        List<ResponseFile> files = pictureStorageService.getAllPicturesById(itemId).map(dbFile -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/files/")
                    .path(dbFile.getId().toString())
                    .toUriString();

            // Creates a Response File for the pictures in a list format to the frontend
            return new ResponseFile(
                    dbFile.getName(),
                    fileDownloadUri,
                    dbFile.getType(),
                    dbFile.getData().length,
                    dbFile.getId());
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    /*
     * Get's the picture by picture ID
     */
    @GetMapping("/files/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<byte[]> getFile(@PathVariable Long id) {
        Pictures picture = pictureStorageService.getFile(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + picture.getName() + "\"")
                .body(picture.getData());
    }
    /*
     * This mapping here is a delte mapping, to get rid of a picture, by picture id
     */
    @DeleteMapping("/pictures/{pictureId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ResponseMessage> deletePicture(@PathVariable("pictureId")long pictureId){
        String message = "";
        this.pictureStorageService.deletePictureById(pictureId);
        message = "File Sucessfully deleted";
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));

    }

}



