package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.entity.Like;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("likeRepository")
public interface LikeRepository extends JpaRepository<Like, Long> {
    //returns like ID of opponent's like
    Like findByItemIDSwipedAndItemIDSwiper(Long itemIDSwiped, Long itemIDSwiper);
    Like findByLikeID(long likeID);

}
