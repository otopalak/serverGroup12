package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.MessageStatus;
import ch.uzh.ifi.hase.soprafs21.entity.ChatMessage;
import ch.uzh.ifi.hase.soprafs21.repository.ChatMessageRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ChatMessageService {
    @Autowired private ChatMessageRepository repository;

    public ChatMessage save(ChatMessage chatMessage) {
        chatMessage.setStatus(MessageStatus.RECEIVED);
        repository.save(chatMessage);
        return chatMessage;
    }

    public long countNewMessages(Long matchId) {
        return repository.countByMatchIdAndStatus( matchId,MessageStatus.RECEIVED);
    }

    public List<ChatMessage> findChatMessages(Long matchId) {

        List<ChatMessage> messages = repository.findBymatchId(matchId);

        if(messages.size() > 0) {
            //updateStatuses(senderId, recipientId, MessageStatus.DELIVERED);
        }

        return messages;
    }

    public ChatMessage findById(Long id) throws NotFoundException {
        return repository
                .findById(id)
                .map(chatMessage -> {
                    chatMessage.setStatus(MessageStatus.DELIVERED);
                    return repository.save(chatMessage);
                })
                .orElseThrow(() ->
                        new NotFoundException("can't find message (" + id + ")"));
    }

    public void updateStatuses(Long senderId, Long recipientId, MessageStatus status) {
        /*Query query = new Query(
                Criteria
                        .where("senderId").is(senderId)
                        .and("recipientId").is(recipientId));
        Update update = Update.update("status", status);
        mongoOperations.updateMulti(query, update, ChatMessage.class);*/
    }
}