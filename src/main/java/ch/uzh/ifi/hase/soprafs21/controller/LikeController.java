package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.Like;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LikePostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.LikeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class LikeController {

    private final LikeService likeService;

    LikeController (LikeService likeService){
        this.likeService = likeService;
    }

    @PostMapping("/likes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void swipe(@RequestBody LikePostDTO likePostDTO){
        if (likePostDTO.getLiked() == null || likePostDTO.getItemIDSwiped() == null || likePostDTO.getItemIDSwiper() == null){
            String baseErrorMessage = "swipe request invalid";
            throw new ResponseStatusException(HttpStatus.CONFLICT,String.format(baseErrorMessage));

        }
        //convert API like to internal representation
        Like likeInput = DTOMapper.INSTANCE.convertLikePostDTOToEntity(likePostDTO);

        // create Like
        likeService.createLike(likeInput);
    }

    @DeleteMapping("/likes/{likeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void deleteLike(@PathVariable("likeId") long likeId){
        likeService.deleteLikeByLikeId(likeId);
    }



}
