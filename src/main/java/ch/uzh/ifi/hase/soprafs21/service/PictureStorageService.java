package ch.uzh.ifi.hase.soprafs21.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import ch.uzh.ifi.hase.soprafs21.entity.Pictures;
import ch.uzh.ifi.hase.soprafs21.repository.PictureDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PictureStorageService {

    @Autowired
    private PictureDBRepository pictureDBRepository;

    // Receives MultipartFile object, transform to a picture Object and saves it to Database
    public Pictures store(MultipartFile file,Long id) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Pictures picture = new Pictures(fileName, file.getContentType(), file.getBytes(),id);
        pictureDBRepository.save(picture);
        return picture;
    }

    // Returns a Picture object by provided id
    public Pictures getFile(Long id) {
        return pictureDBRepository.findById(id).get();
    }

    // Returns all Picture objects in our Database
    public List<Pictures> getAllFiles() {
        return pictureDBRepository.findAll();
    }

    // Get all pictures from an Item
    public Stream<Pictures> getAllPicturesById(long id){
        return this.pictureDBRepository.findAllByItemId(id).stream();
    }
    // Delete a picture by picture id
    public void deletePictureById(long id){
        Pictures picture = this.pictureDBRepository.findById(id);
        this.pictureDBRepository.delete(picture);
    }
}