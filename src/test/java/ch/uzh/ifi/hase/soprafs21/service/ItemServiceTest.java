package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.Item;
import ch.uzh.ifi.hase.soprafs21.entity.Tags;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.ItemRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemServiceTest {
    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;
    private TagsService tagsService;

    private Item testitem;
    private Tags tag;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        // given
        tag = new Tags();
        tag.setDescription("Test");
        List<Tags> tags = new ArrayList<>();
        tags.add(tag);

        testitem = new Item();
        testitem.setId(1L);
        testitem.setDescription("Test Description");
        testitem.setTitle("Test Title");
        testitem.setItemtags(tags);

        // when -> any object is being save in the userRepository -> return the dummy testUser
        Mockito.when(itemRepository.save(Mockito.any())).thenReturn(testitem);
    }

    @Test
    public void createItem_validInputs_success(){
        // When -> any object is being saved in the itemRepository -> return the dummy testUser
        Item iteamcreated = itemService.createItem(testitem);
        // then
        Mockito.verify(itemRepository,Mockito.times(1)).save(Mockito.any());

        assertEquals(testitem.getId(), iteamcreated.getId());
        assertEquals(testitem.getDescription(),iteamcreated.getDescription());
        assertEquals(testitem.getTitle(),iteamcreated.getTitle());
        assertEquals(testitem.getItemtags(),iteamcreated.getItemtags());
    }

}
