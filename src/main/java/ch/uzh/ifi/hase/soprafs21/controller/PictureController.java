package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.Pictures;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.Response.ResponseMessage;
import ch.uzh.ifi.hase.soprafs21.rest.dto.PictureGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.PictureStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

// Needed for the URI, that is sent back
@CrossOrigin("http://localhost:8080")
@RestController
public class PictureController {

    @Autowired
    private PictureStorageService pictureStorageService;

    // This mapping is for uploading a File into our Database
    // Creates a Multipartfile and tries to save it into our Database and returns a respond message
    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        try {
            pictureStorageService.store(file);
            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    /*
     * This mapping is for getting all pictures from the Database
     */
    @GetMapping("/pictures")
    @ResponseStatus(HttpStatus.OK)
    public List<PictureGetDTO> getListofAllPictures(){
        List<Pictures> pictures = pictureStorageService.getAllFiles();
        List<PictureGetDTO> pictureGetDTOS = new ArrayList<>();

        // convert each Picture Entity to the API representation
        for (Pictures picture : pictures) {
            // fromCurrentContextPath(), localhost:8080/pictures/1 etc
            String fileDownloadUri =  ServletUriComponentsBuilder.fromCurrentContextPath().path("/pictures/").path(picture.getId().toString()).toUriString();
            PictureGetDTO pictureGetDTO = DTOMapper.INSTANCE.convertEntityToPictureGetDTO(picture);
            pictureGetDTO.setSize((long) picture.getData().length);
            pictureGetDTO.setUrl(fileDownloadUri);
            pictureGetDTOS.add(pictureGetDTO);
        }
        return pictureGetDTOS;
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id) {
        Pictures picture = pictureStorageService.getFile(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + picture.getName() + "\"")
                .body(picture.getData());
    }
}



