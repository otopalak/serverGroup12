package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.Tags;
import ch.uzh.ifi.hase.soprafs21.repository.ItemRepository;
import ch.uzh.ifi.hase.soprafs21.repository.TagsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebAppConfiguration
@SpringBootTest
public class TagIntegrationTest {

    @Qualifier("tagsRepository")
    @Autowired
    private TagsRepository tagsRepository;

    @Autowired
    private TagsService tagsService;

    @BeforeEach
    public void setup(){
        tagsRepository.deleteAll();
    }

    /*
     * Checks the create Tag function
     */
    @Test
    @Disabled
    public void createTag_validInputs_success(){
        // Creating a Tag
        Tags tag = new Tags();
        tag.setDescription("Tag");

        Tags created = tagsService.createTag(tag);

        assertEquals(tag.getDescription(),created.getDescription());
        assertEquals(tag.getId(),created.getId());
    }
    /*
     * This test will check the (does tag exist allready function)
     * It will thus throw an error, as the tag is allready inside the DB
     */
    @Test
    @Disabled
    public void createTagFails_invalidInput(){
        Tags tag = new Tags();
        tag.setDescription("Tag");
        tagsService.createTag(tag);
        // We now expect an error if we create another tag with the same description:
        Tags tag2 = new Tags();
        tag2.setDescription("Tag");

        assertThrows(ResponseStatusException.class, () -> tagsService.createTag(tag2));
    }
    /*
     * This test checks the getALLTags functionality
     */
    @Test
    @Disabled
    public void getAllTags_returnsAllTags(){
        // When
        Tags tag = new Tags();
        tag.setDescription("Tag");
        tagsService.createTag(tag);

        List<Tags> tags = tagsService.getAllTags();

        assertEquals(tags.get(0).getDescription(),tag.getDescription());
    }
    /*
     * This function will check the function getTagByDescription()
     */
    @Test
    @Disabled
    public void getTag_byDescription_validInput(){
        Tags tag = new Tags();
        tag.setDescription("Tag");
        tagsService.createTag(tag);

        Tags tagwanted = tagsService.getTagByDescription("Tag");

        assertEquals(tag.getId(),tagwanted.getId());
        assertEquals(tag.getDescription(),tagwanted.getDescription());
    }
    /*
     * Same test now with a wrong input -> Descripton not found in a tag
     */
    @Test
    @Disabled
    public void getTag_byDescription_invalidvalidInput(){
        Tags tag = new Tags();
        tag.setDescription("Tag");
        tagsService.createTag(tag);

        assertThrows(ResponseStatusException.class, () -> tagsService.getTagByDescription("test"));

    }

}
