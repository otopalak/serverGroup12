package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.SwapConfirmation;
import ch.uzh.ifi.hase.soprafs21.rest.dto.SwapConfirmationPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.SwapConfirmationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class SwapConfirmationController {

    private SwapConfirmationService swapConfirmationService;

    @Autowired
    SwapConfirmationController(SwapConfirmationService swapConfirmationService){
        this.swapConfirmationService=swapConfirmationService;
    }

    @PostMapping("/swap")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void swap(@RequestBody SwapConfirmationPostDTO swapConfirmationPostDTO){
        if (swapConfirmationPostDTO.getItemID1() == null || swapConfirmationPostDTO.getItemID2() == null){
            String baseErrorMessage = "swap request invalid";
            throw new ResponseStatusException(HttpStatus.CONFLICT,String.format(baseErrorMessage));
        }

        //check if SwapConfirmationExists
        SwapConfirmation swapInput = DTOMapper.INSTANCE.convertSwapConfirmationPostDTOToEntity(swapConfirmationPostDTO);

        // create SwapConfirmation
        SwapConfirmation swapLike = swapConfirmationService.createSwapConfirmation(swapInput);
    }

    @PutMapping("/swap/cancel/{itemID1}/{itemID2}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void cancel(@PathVariable("itemID1") long itemID1, @PathVariable("itemID2") long itemID2) {
        swapConfirmationService.cancelSwapConfirmation(itemID1, itemID2);
    }
}
