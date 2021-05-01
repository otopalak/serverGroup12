package ch.uzh.ifi.hase.soprafs21.service;


import ch.uzh.ifi.hase.soprafs21.repository.PictureDBRepository;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.Test;
import org.mapstruct.Qualifier;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
@WebAppConfiguration
@SpringBootTest
public class PictureIntegrationTest {

    @Autowired
    private PictureDBRepository pictureDBRepository;

    @Autowired
    private PictureStorageService pictureStorageService;

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    /*
     * THis test checks the upload of a picture -> Here we do not have an item, that goes with it, that is why
     * it will throw an error.
     */
    @Test
    public void upload_picture_validInput_noItem(){
        // Creates a Multipart File
        MockMultipartFile multiPFImage = new MockMultipartFile("abcpic.png", "abcpic.png",
       "image/png", "Generate bytes to simulate a picture".getBytes());

        Exception exception = assertThrows(ResponseStatusException.class, () -> pictureStorageService.uploadItemImage(1L, multiPFImage));
        assertEquals(exception.getMessage(),HttpStatus.CONFLICT+ " \"The item does not exist, thus you cannot add pictues!\"");

    }
    }

