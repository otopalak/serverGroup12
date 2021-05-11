package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.SwapPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.SwapConfirmationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class SwapConfirmationController {

    private SwapConfirmationService swapConfirmationService;

    @Autowired
    SwapConfirmationController(SwapConfirmationService swapConfirmationService){
        this.swapConfirmationService=swapConfirmationService;
    }

    //item1 confirms that the swap (item exchange) has taken place
    @PutMapping("/swap/confirm/{itemID1}/{itemID2}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void confirmation(@PathVariable("itemID1") long itemID1, @PathVariable("itemID2") long itemID2) {
        swapConfirmationService.confirm(itemID1, itemID2);
    }
}
