package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Item;
import ch.uzh.ifi.hase.soprafs21.entity.Like;
import ch.uzh.ifi.hase.soprafs21.entity.Matches;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation works.
 */
public class DTOMapperTest {
    @Test
    public void testCreateUser_fromUserPostDTO_toUser_success() {
        // create UserPostDTO
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setName("name");
        userPostDTO.setUsername("username");

        // MAP -> Create user
        User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // check content
        assertEquals(userPostDTO.getName(), user.getName());
        assertEquals(userPostDTO.getUsername(), user.getUsername());
    }

    @Test
    public void testGetUser_fromUser_toUserGetDTO_success() {
        // create User
        User user = new User();
        user.setName("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);
        user.setToken("1");

        // MAP -> Create UserGetDTO
        UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

        // check content
        assertEquals(user.getId(), userGetDTO.getId());
        assertEquals(user.getName(), userGetDTO.getName());
        assertEquals(user.getUsername(), userGetDTO.getUsername());
        assertEquals(user.getStatus(), userGetDTO.getStatus());
    }

    @Test
    public void testCreateItem_fromItemPostDTO_success(){
        ItemPostDTO itemPostDTO = new ItemPostDTO();
        itemPostDTO.setDescription("Test");
        itemPostDTO.setTitle("Title");
        itemPostDTO.setUserId(1L);

        Item item = DTOMapper.INSTANCE.convertItemPostDTOtoEntity(itemPostDTO);

        // check content
        assertEquals(itemPostDTO.getDescription(),item.getDescription());
        assertEquals(itemPostDTO.getTitle(),item.getTitle());
        assertEquals(itemPostDTO.getUserId(),item.getUserId());
    }

    @Test
    public void testConvertEntity_to_ItemGETDTO_success(){
        Item item = new Item();
        item.setId(1L);
        item.setUserId(1L);
        item.setDescription("Test");
        item.setTitle("Title");

        ItemGetDTO itemGetDTO = DTOMapper.INSTANCE.convertEntityToItemGetDTO(item);

        // check content
        assertEquals(itemGetDTO.getDescription(),item.getDescription());
        assertEquals(itemGetDTO.getTitle(),item.getTitle());
        assertEquals(itemGetDTO.getUserId(),item.getUserId());
        assertEquals(itemGetDTO.getId(),item.getId());

    }

    @Test
    public void success_ItemPUTDTOtoEntity(){
        ItemPutDTO itemPutDTO = new ItemPutDTO();
        itemPutDTO.setTitle("Hello");
        itemPutDTO.setDescription("Description");

        Item item = DTOMapper.INSTANCE.convertItemPutDTOtoEntity(itemPutDTO);

        assertEquals(itemPutDTO.getDescription(),item.getDescription());
        assertEquals(itemPutDTO.getTitle(),item.getTitle());

    }

    @Test
    public void success_convertEntityToMatchesGetDTO(){
        Matches match = new Matches();
        match.setId(1L);
        match.setItemIdOne(1L);
        match.setItemIdTwo(2L);

        MatchesGetDTO matchesGetDTO = DTOMapper.INSTANCE.convertEntityToMatchesGetDTO(match);

        assertEquals(matchesGetDTO.getId(),match.getId());
        assertEquals(matchesGetDTO.getItemIdOne(),match.getItemIdOne());
        assertEquals(matchesGetDTO.getItemIdTwo(),match.getItemIdTwo());
    }

    @Test
    public void success_convertLikePostDTOToEntity(){
        LikePostDTO likePostDTO = new LikePostDTO();
        likePostDTO.setLiked(true);
        likePostDTO.setItemIDSwiped(1l);
        likePostDTO.setItemIDSwiper(2L);

        Like like = DTOMapper.INSTANCE.convertLikePostDTOToEntity(likePostDTO);

        assertEquals(likePostDTO.getItemIDSwiped(),like.getItemIDSwiped());
        assertEquals(likePostDTO.getItemIDSwiper(),like.getItemIDSwiper());
        assertEquals(likePostDTO.getLiked(),like.getLiked());
    }





}
