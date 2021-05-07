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
    private Boolean swapConfirmed1;

    @Column(nullable = false)
    private Boolean swapConfirmed2;

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

    public Boolean getSwapConfirmed1() {
        return swapConfirmed1;
    }

    public void setSwapConfirmed1(Boolean swapConfirmed1) {
        this.swapConfirmed1 = swapConfirmed1;
    }

    public Boolean getSwapConfirmed2() {
        return swapConfirmed2;
    }

    public void setSwapConfirmed2(Boolean swapConfirmed2) {
        this.swapConfirmed2 = swapConfirmed2;
    }
}
