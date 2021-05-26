package ch.uzh.ifi.hase.soprafs21.repository;
import ch.uzh.ifi.hase.soprafs21.entity.Item;
import ch.uzh.ifi.hase.soprafs21.entity.SwapConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("swapConfirmationRepository")
public interface SwapConfirmationRepository extends JpaRepository<SwapConfirmation, Long> {
    SwapConfirmation findByItemID1AndAndItemID2(Long itemID1, Long itemID2);
    List<SwapConfirmation> findByItemID1(Long itemID1);
    List<SwapConfirmation> findByItemID2(Long itemID2);

}
