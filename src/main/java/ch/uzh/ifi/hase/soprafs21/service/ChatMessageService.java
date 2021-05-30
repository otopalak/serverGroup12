package ch.uzh.ifi.hase.soprafs21.service;

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
        repository.save(chatMessage);
        return chatMessage;
    }

    public List<ChatMessage> findChatMessages(Long matchId) {

        List<ChatMessage> messages = repository.findBymatchId(matchId);
        return messages;
    }

    public ChatMessage findById(Long id) throws NotFoundException {
        return repository
                .findById(id)
                .map(chatMessage -> {
                    return repository.save(chatMessage);
                })
                .orElseThrow(() ->
                        new NotFoundException("can't find message (" + id + ")"));
    }
}