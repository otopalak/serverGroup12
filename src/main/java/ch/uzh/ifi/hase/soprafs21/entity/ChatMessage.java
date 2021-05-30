package ch.uzh.ifi.hase.soprafs21.entity;

import javax.persistence.*;
import java.util.Date;

/*
 *  This is a picture Entity to save pictures in the Database
 */

@Entity
@Table(name="chatMessage")
public class ChatMessage {
    @Id
    @GeneratedValue
    private Long id;
    private Long senderId;
    private Long matchId;
    private Long recipientId;
    private String senderName;
    private String recipientName;
    private String content;
    private Date timestamp;

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

}