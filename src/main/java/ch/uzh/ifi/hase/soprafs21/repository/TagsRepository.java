package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("tagsRepository")
public interface TagsRepository extends JpaRepository<Tags, Long> {
    Tags getTagsByDescription(String description);
}
