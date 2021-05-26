package ch.uzh.ifi.hase.soprafs21.rest.dto;

public class SwapConfirmationPostDTO {
    private Long itemID1;
    private Long itemID2;
    private Boolean item1ConfirmsItem2;
    private Boolean item2ConfirmsItem1;

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

    public Boolean getItem1ConfirmsItem2() {
        return item1ConfirmsItem2;
    }

    public void setItem1ConfirmsItem2(Boolean item1ConfirmsItem2) {
        this.item1ConfirmsItem2 = item1ConfirmsItem2;
    }

    public Boolean getItem2ConfirmsItem1() {
        return item2ConfirmsItem1;
    }

    public void setItem2ConfirmsItem1(Boolean item2ConfirmsItem1) {
        this.item2ConfirmsItem1 = item2ConfirmsItem1;
    }
}
