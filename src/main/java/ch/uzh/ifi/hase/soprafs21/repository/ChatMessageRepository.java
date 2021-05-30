package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("chatMessageRepository")
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
        List<ChatMessage> findBymatchId(Long matchId);
}
