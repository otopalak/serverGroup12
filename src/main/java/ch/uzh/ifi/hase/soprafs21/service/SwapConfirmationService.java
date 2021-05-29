package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SwapConfirmationService {
    private SwapConfirmationRepository swapConfirmationRepository;
    private LikeRepository likeRepository;
    private MatchRepository matchRepository;
    private ItemRepository itemRepository;

    @Autowired
    public SwapConfirmationService(@Qualifier("swapConfirmationRepository") SwapConfirmationRepository swapConfirmationRepository,
                                   @Qualifier("likeRepository")LikeRepository likeRepository,
                                   @Qualifier("matchRepository")MatchRepository matchRepository,
                                   @Qualifier("itemRepository")ItemRepository itemRepository){
        this.swapConfirmationRepository = swapConfirmationRepository;
        this.likeRepository = likeRepository;
        this.matchRepository = matchRepository;
        this.itemRepository = itemRepository;
    }

    public SwapConfirmation createSwapConfirmation(SwapConfirmation swapConfirmation){
        //check if already exists
        long itemID1 = swapConfirmation.getItemID1();
        long itemID2 = swapConfirmation.getItemID2();

        SwapConfirmation swapConfirmation1 = swapConfirmationRepository.findByItemID1AndItemID2(itemID1, itemID2);
        SwapConfirmation swapConfirmation2 = swapConfirmationRepository.findByItemID1AndItemID2(itemID2, itemID1);

        if(swapConfirmation1 != null){
            swapConfirmation1.setItem1ConfirmsItem2(true);
            changeItemsIfConfirmed(swapConfirmation1);
            return swapConfirmation1;
        }

        if(swapConfirmation2 != null){
            swapConfirmation2.setItem2ConfirmsItem1(true);
            changeItemsIfConfirmed(swapConfirmation2);
            return swapConfirmation2;
        }

        //create SwapConfirmation
        SwapConfirmation newSwapConfirmation = new SwapConfirmation();
        newSwapConfirmation.setItemID1(itemID1);
        newSwapConfirmation.setItemID2(itemID2);
        newSwapConfirmation.setItem1ConfirmsItem2(true);
        newSwapConfirmation.setItem2ConfirmsItem1(false);

        //save SwapConfirmation
        SwapConfirmation savedSwapConfirmation = swapConfirmationRepository.save(newSwapConfirmation);
        swapConfirmationRepository.flush();

        return savedSwapConfirmation;
    }

    public String cancelSwapConfirmation(long itemID1, long itemID2){
        // search for a SwapConfirmation
        SwapConfirmation swapConfirmation1 = swapConfirmationRepository.findByItemID1AndItemID2(itemID1, itemID2);
        SwapConfirmation swapConfirmation2 = swapConfirmationRepository.findByItemID1AndItemID2(itemID2, itemID1);

        //Swap confirmation does not exist
        if (swapConfirmation1 == null && swapConfirmation2 == null){
            String baseErrorMessage = "SwapConfirmation does not exist!";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage));
        }
        // check which Swap confirmation is not empty and set the confirmation
        if (swapConfirmation1 != null){
            swapConfirmation1.setItem1ConfirmsItem2(false);
        }
        if (swapConfirmation2 != null){
            swapConfirmation2.setItem1ConfirmsItem2(false);
        }
        return "swap canceled";
    }

    public void changeItemsIfConfirmed(SwapConfirmation swapConfirmation){
        //if both users confirm that they actually swapped the item
        //we them want to assign the items to the other person

        //check that both users have confirmed the swap
        if (swapConfirmation.getItem1ConfirmsItem2() && swapConfirmation.getItem2ConfirmsItem1()){
            long itemIDUser1 = swapConfirmation.getItemID1();
            long itemIDUser2 = swapConfirmation.getItemID2();

            //clear Like entities of both items
            List<Like> likesToDelete = new ArrayList<>();
            likesToDelete.addAll(likeRepository.findAllByItemIDSwiper(itemIDUser1));
            likesToDelete.addAll(likeRepository.findAllByItemIDSwiper(itemIDUser2));
            likesToDelete.addAll(likeRepository.findAllByItemIDSwiped(itemIDUser1));
            likesToDelete.addAll(likeRepository.findAllByItemIDSwiped(itemIDUser2));

            likeRepository.deleteAll(likesToDelete);

            //clear Match entities of both items
            List<Matches> matchesOfItem1 = new ArrayList<>();
            matchesOfItem1.addAll(matchRepository.findByItemIdOne(itemIDUser1));
            matchesOfItem1.addAll(matchRepository.findByItemIdTwo(itemIDUser1));

            List<Matches> matchesOfItem2 = new ArrayList<>();
            matchesOfItem2.addAll(matchRepository.findByItemIdOne(itemIDUser2));
            matchesOfItem2.addAll(matchRepository.findByItemIdTwo(itemIDUser2));

            matchRepository.deleteAll(matchesOfItem1);
            matchRepository.deleteAll(matchesOfItem2);

            //SwapHistory
            Item item1 = itemRepository.findById(itemIDUser1);
            Item item2 = itemRepository.findById(itemIDUser2);

            String itemTitle1 = item1.getTitle();
            String itemTitle2 = item2.getTitle();

            List<String> swapHistory1 = item1.getSwapHistory();
            swapHistory1.add(itemTitle1);

            List<String> swapHistory2 = item2.getSwapHistory();
            swapHistory2.add(itemTitle2);

            item1.setSwapHistory(swapHistory2);
            item2.setSwapHistory(swapHistory1);

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

            //and all other SwapConfirmations with both items
            List<SwapConfirmation> swapConfirmationsToDelete = new ArrayList<>();
            swapConfirmationsToDelete.addAll(swapConfirmationRepository.findByItemID1(itemIDUser1));
            swapConfirmationsToDelete.addAll(swapConfirmationRepository.findByItemID1(itemIDUser2));
            swapConfirmationsToDelete.addAll(swapConfirmationRepository.findByItemID2(itemIDUser1));
            swapConfirmationsToDelete.addAll(swapConfirmationRepository.findByItemID2(itemIDUser2));

            swapConfirmationRepository.deleteAll(swapConfirmationsToDelete);
        }
    }

    public boolean check(long ownItemID, long matchedItemID) {
        SwapConfirmation swapConfirmation1 = swapConfirmationRepository.findByItemID1AndItemID2(ownItemID, matchedItemID);
        SwapConfirmation swapConfirmation2 = swapConfirmationRepository.findByItemID1AndItemID2(matchedItemID, ownItemID);

        if (swapConfirmation1 != null){
            return swapConfirmation1.getItem1ConfirmsItem2();
        }

        if (swapConfirmation2 != null){
            return swapConfirmation2.getItem2ConfirmsItem1();
        }
        return false;
    }
}
