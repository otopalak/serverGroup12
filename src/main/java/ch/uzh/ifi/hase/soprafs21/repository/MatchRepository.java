package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.entity.Matches;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("matchRepository")
public interface MatchRepository extends JpaRepository<Matches, Long> {
    Matches findById(long id);
    List<Matches> findByItemIdOne(long itemIdOne);
    List<Matches> findByItemIdTwo(long itemIdTwo);
}