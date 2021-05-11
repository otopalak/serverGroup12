package ch.uzh.ifi.hase.soprafs21.rest.dto;

public class SwapPostDTO {
    private Long itemID1;
    private Long itemID2;
    private Boolean decision;

    public Long getItemID1() {
        return itemID1;
    }

    public void setItemID1(Long itemID1) {
        this.itemID1 = itemID1;
    }

    public Long getItemID2() {
        return itemID2;
    }

    public void setItemID2(Long itemID2) {
        this.itemID2 = itemID2;
    }

    public Boolean getDecision() {
        return decision;
    }

    public void setDecision(Boolean decision) {
        this.decision = decision;
    }
}
