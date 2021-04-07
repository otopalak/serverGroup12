package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.ChatMessage;
import ch.uzh.ifi.hase.soprafs21.constant.ChatNotification;
import ch.uzh.ifi.hase.soprafs21.service.ChatMessageService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ChatController {

    @Autowired private SimpMessagingTemplate messagingTemplate;
    @Autowired private ChatMessageService chatMessageService;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        ChatMessage saved = chatMessageService.save(chatMessage);
        messagingTemplate.convertAndSendToUser(
               String.valueOf(chatMessage.getRecipientId()),"/queue/messages",
                new ChatNotification(
                        saved.getId(),
                        saved.getSenderId(),
                        saved.getSenderName()));
    }

    @GetMapping("/messages/{matchId}/count")
    public ResponseEntity<Long> countNewMessages(@PathVariable Long matchId) {
        return ResponseEntity
                .ok(chatMessageService.countNewMessages(matchId));
    }

    @GetMapping("/messages/{matchId}")
    public ResponseEntity<?> findChatMessages ( @PathVariable Long matchId){
        return ResponseEntity
                .ok(chatMessageService.findChatMessages(matchId));
    }

    @GetMapping("/message/{id}")
    public ResponseEntity<?> findMessage ( @PathVariable Long id) throws NotFoundException {
        return ResponseEntity
                .ok(chatMessageService.findById(id));
    }
}
