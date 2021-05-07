package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.Swap;
import ch.uzh.ifi.hase.soprafs21.entity.SwapConfirmation;
import ch.uzh.ifi.hase.soprafs21.repository.SwapConfirmationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

@Service
@Transactional
public class SwapConfirmationService {
    private SwapConfirmationRepository swapConfirmationRepository;

    @Autowired
    public SwapConfirmationService(@Qualifier("swapConfirmationRepository") SwapConfirmationRepository swapConfirmationRepository){
        this.swapConfirmationRepository = swapConfirmationRepository;
    }

    public void createSwapConfirmation(Long itemID1, Long itemID2){
        //create SwapConfirmation
        SwapConfirmation swapConfirmation = new SwapConfirmation();
        swapConfirmation.setItemID1(itemID1);
        swapConfirmation.setItemID2(itemID2);
        swapConfirmation.setSwapConfirmed1(false);
        swapConfirmation.setSwapConfirmed2(false);

        //save Swap
        swapConfirmationRepository.save(swapConfirmation);
        swapConfirmationRepository.flush();
    }

    public String confirm(Long itemID1, Long itemID2){
        // search for a SwapConfirmation
        SwapConfirmation swapConfirmation1 = swapConfirmationRepository.findByItemID1AndAndItemID2(itemID1, itemID2);
        SwapConfirmation swapConfirmation2 = swapConfirmationRepository.findByItemID1AndAndItemID2(itemID2, itemID1);

        //Swap confirmation does not exist
        if (swapConfirmation1 == null && swapConfirmation2 == null){
            String baseErrorMessage = "SwapConfirmation does not exist!";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage));
        }
        // check which Swap confirmation is not empty and set the confirmation
        if (swapConfirmation1 != null){
            if (swapConfirmation1.getItemID1() == itemID1){
                swapConfirmation1.setSwapConfirmed1(true);
            }else{
                swapConfirmation1.setSwapConfirmed2(true);
            }
        }
        if (swapConfirmation2 != null){
            if (swapConfirmation2.getItemID1() == itemID1){
                swapConfirmation2.setSwapConfirmed1(true);
            }else
                swapConfirmation2.setSwapConfirmed2(true);
        }
        return "swap confirmed";

    }

}
