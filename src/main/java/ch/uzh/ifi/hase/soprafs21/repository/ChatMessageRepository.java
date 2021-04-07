package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.constant.MessageStatus;
import ch.uzh.ifi.hase.soprafs21.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.logging.LogManager;

@Repository("chatMessageRepository")
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    long countBySenderIdAndRecipientIdAndStatus(
            Long senderId, Long recipientId, MessageStatus status);
    List<ChatMessage> findBymatchId(Long matchId);
}
