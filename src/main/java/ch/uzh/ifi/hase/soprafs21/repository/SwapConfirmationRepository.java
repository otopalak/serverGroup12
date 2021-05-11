package ch.uzh.ifi.hase.soprafs21.repository;
import ch.uzh.ifi.hase.soprafs21.entity.Item;
import ch.uzh.ifi.hase.soprafs21.entity.SwapConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("swapConfirmationRepository")
public interface SwapConfirmationRepository extends JpaRepository<SwapConfirmation, Long> {
    SwapConfirmation findByItemID1AndAndItemID2(Long itemID1, Long itemID2);

}
