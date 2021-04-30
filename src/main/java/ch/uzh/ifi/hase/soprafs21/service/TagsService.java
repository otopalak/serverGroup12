package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.Tags;
import ch.uzh.ifi.hase.soprafs21.repository.TagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class TagsService {

    @Autowired
    private TagsRepository tagsRepository;

    // Checks if the tag already exists or not
    public boolean checkIfTagExists(String description){
        Tags tag = this.tagsRepository.getTagsByDescription(description);
        if(tag != null){
            return true;
        }else{

            return false;
        }
    }

    // This creates a tag
    public Tags createTag(Tags tag){
        Boolean exists = this.checkIfTagExists(tag.getDescription());
        if(exists==true){
            String baseErrorMessage = "This tag allready exists";
            throw new ResponseStatusException(HttpStatus.CONFLICT,String.format(baseErrorMessage));
        }else{
            this.tagsRepository.save(tag);
            return tag;
        }
    }

    // Get all tags
    public List<Tags> getAllTags(){
        List<Tags> tags = this.tagsRepository.findAll();
        return tags;
    }

    // Get tag by Description
    public Tags getTagByDescription(String description){
        Tags tag = this.tagsRepository.getTagsByDescription(description);
        if(tag==null){
            String baseErrorMessage = "This tag doesn't exist!";
            throw new ResponseStatusException(HttpStatus.CONFLICT,String.format(baseErrorMessage));
        }
        return tag;
    }

}
