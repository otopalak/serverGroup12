package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("likeRepository")
public interface LikeRepository extends JpaRepository<Like, Long> {
    //returns like ID of opponent's like
    Like findByItemIDSwipedAndItemIDSwiper(long itemIDSwiped, long itemIDSwiper);
    Like findByLikeID(long likeID);
    Like findByItemIDSwiper(long itemIDSwiper);
    List<Like> findAllByItemIDSwiper(long itemIDSwiper);
    List<Like> findAllByItemIDSwiped(long itemIDSwiped);
}
