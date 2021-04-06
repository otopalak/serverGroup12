package ch.uzh.ifi.hase.soprafs21.rest.dto;

public class MatchesGetDTO {
    private Long id;
    private Long itemIdOne;
    private Long itemIdTwo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemIdOne() {
        return itemIdOne;
    }

    public void setItemIdOne(Long itemIdOne) {
        this.itemIdOne = itemIdOne;
    }

    public Long getItemIdTwo() {
        return itemIdTwo;
    }

    public void setItemIdTwo(Long itemIdTwo) {
        this.itemIdTwo = itemIdTwo;
    }
}
