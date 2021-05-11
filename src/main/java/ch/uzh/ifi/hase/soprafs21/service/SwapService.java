package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.Swap;
import ch.uzh.ifi.hase.soprafs21.repository.SwapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
@Transactional
public class SwapService {
    private final SwapRepository swapRepository;
    private final SwapConfirmationService swapConfirmationService;

    @Autowired
    public SwapService(@Qualifier("swapRepository") SwapRepository swapRepository, @Qualifier("swapConfirmationService") SwapConfirmationService swapConfirmationService){
        this.swapRepository=swapRepository;
        this.swapConfirmationService=swapConfirmationService;
    }

    public Swap createSwap(Swap swapInput){
        //check for existing Swap
        Long itemID1 = swapInput.getItemID1();
        Long itemID2 = swapInput.getItemID2();

        for(Swap repoSwap: swapRepository.findAll()){
            if (itemID1.equals(repoSwap.getItemID1()) && itemID2.equals(repoSwap.getItemID2())){
                repoSwap.setDecision(swapInput.getDecision());
                return repoSwap;
            }
        }

        Swap newSwap = swapRepository.save(swapInput);
        swapRepository.flush();
        checkSwapConfirmation(newSwap);
        //check for confirmed Swap

        return newSwap;

    }
    public boolean checkSwapConfirmation(Swap swap){
        if (swap.getDecision()) {
            Long itemID1 = swap.getItemID1();
            Long itemID2 = swap.getItemID2();
            Swap otherSwap = swapRepository.findByItemID1AndAndItemID2(itemID2, itemID1);
            if (otherSwap != null && otherSwap.getDecision()){
                swapConfirmationService.createSwapConfirmation(itemID1, itemID2);
                return true;
            }
        }
        return false;
    }

}
