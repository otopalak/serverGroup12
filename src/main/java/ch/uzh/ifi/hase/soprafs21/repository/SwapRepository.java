package ch.uzh.ifi.hase.soprafs21.repository;


import ch.uzh.ifi.hase.soprafs21.entity.Like;
import ch.uzh.ifi.hase.soprafs21.entity.Swap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("swapRepository")
public interface SwapRepository extends JpaRepository<Swap, Long> {
    Swap findByItemID1(Long itemID1);
    Swap findByItemID2(Long itemID2);
    Swap findByItemID1AndAndItemID2(Long itemID1, Long itemID2);
    List<Swap> findAllByItemID1(Long itemID1);
    List<Swap> findAllByItemID2(Long itemIDUser1);
}
