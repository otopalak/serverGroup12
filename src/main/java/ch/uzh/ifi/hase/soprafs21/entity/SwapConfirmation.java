package ch.uzh.ifi.hase.soprafs21.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "SwapConfirmation")
public class SwapConfirmation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long swapConfID;

    @Column(nullable = false)
    private Long itemID1;

    @Column(nullable = false)
    private Long itemID2;

    @Column(nullable = false)
    private Boolean item1ConfirmsItem2;

    @Column(nullable = false)
    private Boolean item2ConfirmsItem1;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getSwapConfID() {
        return swapConfID;
    }

    public void setSwapConfID(Long swapConfID) {
        this.swapConfID = swapConfID;
    }

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

    public void setItem1ConfirmsItem2(Boolean swapConfirmed1) {
        this.item1ConfirmsItem2 = swapConfirmed1;
    }

    public Boolean getItem2ConfirmsItem1() {
        return item2ConfirmsItem1;
    }

    public void setItem2ConfirmsItem1(Boolean swapConfirmed2) {
        this.item2ConfirmsItem1 = swapConfirmed2;
    }
}
