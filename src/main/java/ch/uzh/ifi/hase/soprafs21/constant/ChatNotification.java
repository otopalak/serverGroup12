package ch.uzh.ifi.hase.soprafs21.constant;

public class ChatNotification {
    private Long id;
    private Long senderId;
    private String senderName;

    public ChatNotification(Long id, Long senderId, String senderName) {
        this.id = id;
        this.senderId = senderId;
        this.senderName = senderName;
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

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}