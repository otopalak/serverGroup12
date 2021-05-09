package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.constant.ChatNotification;
import ch.uzh.ifi.hase.soprafs21.constant.Game;
import ch.uzh.ifi.hase.soprafs21.entity.ChatMessage;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GameDTO;
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
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class GameController {

    @Autowired private SimpMessagingTemplate messagingTemplate;
    @Autowired private ChatMessageService chatMessageService;

    @MessageMapping("/game")
    public void processMessage(@RequestBody GameDTO game) {
        //ChatMessage saved = chatMessageService.save(chatMessage);
        messagingTemplate.convertAndSendToUser(
               String.valueOf(game.getRecipientId()),"/queue/game", game);
    }
}
