package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SwapConfirmationService {
    private SwapConfirmationRepository swapConfirmationRepository;
    private SwapRepository swapRepository;
    private LikeRepository likeRepository;
    private MatchRepository matchRepository;
    private ItemRepository itemRepository;

    @Autowired
    public SwapConfirmationService(@Qualifier("swapConfirmationRepository") SwapConfirmationRepository swapConfirmationRepository,
                                   @Qualifier("swapRepository") SwapRepository swapRepository,
                                   @Qualifier("likeRepository")LikeRepository likeRepository,
                                   @Qualifier("matchRepository")MatchRepository matchRepository,
                                   @Qualifier("itemRepository")ItemRepository itemRepository){
        this.swapConfirmationRepository = swapConfirmationRepository;
        this.swapRepository = swapRepository;
        this.likeRepository = likeRepository;
        this.matchRepository = matchRepository;
        this.itemRepository = itemRepository;
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
            changeItemsIfConfirmed(swapConfirmation1);
        }
        if (swapConfirmation2 != null){
            if (swapConfirmation2.getItemID1() == itemID1){
                swapConfirmation2.setSwapConfirmed1(true);
            }else
                swapConfirmation2.setSwapConfirmed2(true);
            changeItemsIfConfirmed(swapConfirmation2);
        }
        return "swap confirmed";

    }

    public void changeItemsIfConfirmed(SwapConfirmation swapConfirmation){
        //if both users confirm that they actually swapped the item
        //we them want to assign the items to the other person

        //check that both users have confirmed the swap
        if (swapConfirmation.getSwapConfirmed1() && swapConfirmation.getSwapConfirmed2()){
            long itemIDUser1 = swapConfirmation.getItemID1();
            long itemIDUser2 = swapConfirmation.getItemID2();

            //clear Like entities of both items
            List<Like> likesOfItem1 = likeRepository.findAllByItemIDSwiper(itemIDUser1);
            List<Like> likesOfItem2 = likeRepository.findAllByItemIDSwiper(itemIDUser2);
            likeRepository.deleteAll(likesOfItem1);
            likeRepository.deleteAll(likesOfItem2);

            //clear Match entities of both items
            List<Matches> matchesOfItem1 = new ArrayList<>();
            matchesOfItem1.addAll(matchRepository.findByItemIdOne(itemIDUser1));
            matchesOfItem1.addAll(matchRepository.findByItemIdTwo(itemIDUser1));

            List<Matches> matchesOfItem2 = new ArrayList<>();
            matchesOfItem2.addAll(matchRepository.findByItemIdOne(itemIDUser2));
            matchesOfItem2.addAll(matchRepository.findByItemIdTwo(itemIDUser2));

            matchRepository.deleteAll(matchesOfItem1);
            matchRepository.deleteAll(matchesOfItem2);

            //clear Swap entities of both items
            List<Swap> swapsOfItem1 = swapRepository.findAllByItemID1(itemIDUser1);
            List<Swap> swapsOfItem2 = swapRepository.findAllByItemID1(itemIDUser2);

            swapRepository.deleteAll(swapsOfItem1);
            swapRepository.deleteAll(swapsOfItem2);

            //and also all swap entities created by other items with those items
            List<Swap> swapsOfOthersWithItem1 = swapRepository.findAllByItemID2(itemIDUser1);
            List<Swap> swapsOfOthersWithItem2 = swapRepository.findAllByItemID2(itemIDUser2);
            swapRepository.deleteAll(swapsOfOthersWithItem1);
            swapRepository.deleteAll(swapsOfOthersWithItem2);

            //assign both items to other user
            if (itemRepository.findById(itemIDUser1)!=null && itemRepository.findById(itemIDUser2)!=null) {
                Item itemOfUser1 = itemRepository.findById(itemIDUser1);
                Item itemOfUser2 = itemRepository.findById(itemIDUser2);
                long userIDOfUser1 = itemOfUser1.getUserId();
                long userIDOfUser2 = itemOfUser2.getUserId();

                itemOfUser1.setUserId(userIDOfUser2);
                itemOfUser2.setUserId(userIDOfUser1);
            }
            //clear SwapConfirmation
            swapConfirmationRepository.delete(swapConfirmation);
        }
    }
}
