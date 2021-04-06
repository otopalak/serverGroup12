package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.ChatRoom;
import ch.uzh.ifi.hase.soprafs21.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatRoomService {

    @Autowired private ChatRoomRepository chatRoomRepository;

    public Long getChatId(
            Long senderId, Long recipientId, boolean createIfNotExist) {
        ChatRoom chatToSearch = chatRoomRepository.findBySenderIdAndRecipientId(senderId, recipientId);
        if(chatToSearch == null){
            String chatId = Long.toString(senderId) + Long.toString(recipientId);
            Long newId = Long.valueOf(chatId).longValue();
            ChatRoom chatRoomOne = new ChatRoom();
            chatRoomOne.setChatId(newId);
            chatRoomOne.setSenderId(senderId);
            chatRoomOne.setRecipientId(recipientId);
            chatRoomRepository.save(chatRoomOne);

            ChatRoom chatRoomTwo = new ChatRoom();
            chatRoomTwo.setChatId(newId);
            chatRoomTwo.setSenderId(recipientId);
            chatRoomTwo.setRecipientId(senderId);
            chatRoomRepository.save(chatRoomTwo);
            return newId;
        }


        return chatToSearch.getChatId();

    }
}
