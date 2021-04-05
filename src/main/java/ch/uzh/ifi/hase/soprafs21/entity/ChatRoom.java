package ch.uzh.ifi.hase.soprafs21.entity;

import javax.persistence.*;

/*
 *  This is a picture Entity to save pictures in the Database
 */

@Entity
@Table(name="chatRoom")
public class ChatRoom {
    @Id
    @GeneratedValue
    private Long id;
    private Long chatId;
    private Long senderId;
    private Long recipientId;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }
}