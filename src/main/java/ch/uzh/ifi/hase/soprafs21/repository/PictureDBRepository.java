package ch.uzh.ifi.hase.soprafs21.repository;
import ch.uzh.ifi.hase.soprafs21.entity.Pictures;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * Pictures Db Repository for storing the pictures
 */
@Repository("pictureDBRepository")
public interface PictureDBRepository extends JpaRepository<Pictures, Long> {
    List<Pictures> findAllByItemId(long itemId);
    Pictures findById(long id);
    Pictures findByName(String filename);
    void deleteAllByItemId(long id);

}
