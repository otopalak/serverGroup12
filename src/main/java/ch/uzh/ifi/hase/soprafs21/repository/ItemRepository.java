package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository("itemRepository")
public interface ItemRepository extends JpaRepository<Item, Long> {
    Item findById(long id);
}

