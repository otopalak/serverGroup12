package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.Tags;
import ch.uzh.ifi.hase.soprafs21.rest.dto.TagGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.TagPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TagController {

    @Autowired
    private TagsService tagsService;


    // Mapping to create a Tag
    @PostMapping("/tag")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public TagGetDTO createTag(@RequestBody TagPostDTO tagPostDTO){
        Tags tag = DTOMapper.INSTANCE.convertTagPostDTOtoEntity(tagPostDTO);
        Tags tag1 = this.tagsService.createTag(tag);
        TagGetDTO tagGetDTO = DTOMapper.INSTANCE.convertEntityToTagGetDTO(tag1);
        return tagGetDTO;
    }

    // Mapping to get all Tags
    @GetMapping("/tags")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<TagGetDTO> getAllTags(){
        List<Tags> tags = this.tagsService.getAllTags();
        List<TagGetDTO> tagGetDTOs = new ArrayList<>();
        // Converting all Tags to a representation understood by API
        for(Tags tag: tags){
            tagGetDTOs.add(DTOMapper.INSTANCE.convertEntityToTagGetDTO(tag));
        }
        return tagGetDTOs;
    }

}
