package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.Like;
import ch.uzh.ifi.hase.soprafs21.entity.Swap;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LikePostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.SwapPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.SwapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class SwapController {

    private SwapService swapService;

    @Autowired
    public SwapController (SwapService swapService) {this.swapService=swapService;}


    @PostMapping("/swap")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void swap(@RequestBody SwapPostDTO swapPostDTO){
        if (swapPostDTO.getDecision() == null || swapPostDTO.getItemID1() == null || swapPostDTO.getItemID2() == null){
            String baseErrorMessage = "swap request invalid";
            throw new ResponseStatusException(HttpStatus.CONFLICT,String.format(baseErrorMessage));

        }
        //convert API like to internal representation
        Swap swapInput = DTOMapper.INSTANCE.convertSwapPostDTOToEntity(swapPostDTO);

        // create Swap
        Swap swapLike = swapService.createSwap(swapInput);

    }




}
