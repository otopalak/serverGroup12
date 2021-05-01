package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.rest.dto.*;
import org.apache.tomcat.jni.Address;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g., UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for creating information (POST).
 */
@Mapper
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "address",target = "address")
    @Mapping(source = "city",target = "city")
    @Mapping(source = "postcode",target = "postcode")
    User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

    @Mapping(source = "title", target = "title")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "description", target = "description")
    Item convertItemPostDTOtoEntity(ItemPostDTO itemPostDTO);

    @Mapping(source = "id",target = "id")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "title", target = "title")
    ItemGetDTO convertEntityToItemGetDTO(Item item);

    @Mapping(source = "description", target = "description")
    @Mapping(source = "title", target = "title")
    Item convertItemPutDTOtoEntity(ItemPutDTO itemPutDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "token",target = "token")
    @Mapping(source = "address",target = "address")
    @Mapping(source = "city",target = "city")
    @Mapping(source = "postcode",target = "postcode")
    UserGetDTO convertEntityToUserGetDTO(User user);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "itemIdOne", target = "itemIdOne")
    @Mapping(source = "itemIdTwo", target = "itemIdTwo")
    MatchesGetDTO convertEntityToMatchesGetDTO(Matches matches);

    @Mapping(source = "id",target = "id")
    User convertUserPutDTOtoEntity(UserPutDTO userPutDTO);


    @Mapping(source = "itemIDSwiper", target = "itemIDSwiper")
    @Mapping(source = "itemIDSwiped", target = "itemIDSwiped")
    @Mapping(source = "liked", target = "liked")
    Like convertLikePostDTOToEntity(LikePostDTO likePostDTO);


    @Mapping(source = "id",target = "id")
    @Mapping(source = "description",target = "description")
    TagGetDTO convertEntityToTagGetDTO(Tags tag);

    @Mapping(source = "description",target = "description")
    Tags convertTagPostDTOtoEntity(TagPostDTO tagPostDTO);

    @Mapping(source = "name",target = "name")
    @Mapping(source = "url",target = "url")
    PictureGetDTO convertEntityToPictureGetDTO(Pictures pictures);


}
