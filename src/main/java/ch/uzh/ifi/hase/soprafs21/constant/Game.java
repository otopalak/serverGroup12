package ch.uzh.ifi.hase.soprafs21.constant;

public class Game {
    private Long senderId;
    private String senderName;
    private Boolean request;
    private Boolean accept;
    private Long recipientId;
    private String type;

    public Game(Long senderId, String senderName, Boolean request, Boolean accept, String type, Long recipientId) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.senderName = senderName;
        this.request = request;
        this.accept = accept;
        this.type = type;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
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

    public Boolean getRequest() {
        return request;
    }

    public void setRequest(Boolean request) {
        this.request = request;
    }

    public Boolean getAccept() {
        return accept;
    }

    public void setAccept(Boolean accept) {
        this.accept = accept;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}