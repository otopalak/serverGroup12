package ch.uzh.ifi.hase.soprafs21.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Swap")
public class Swap implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long swapID;

    @Column(nullable = false)
    private Long itemID1;

    @Column(nullable = false)
    private Long itemID2;

    @Column
    private Boolean decision;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getSwapID() {
        return swapID;
    }

    public void setSwapID(Long swapID) {
        this.swapID = swapID;
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

    public Boolean getDecision() {
        return decision;
    }

    public void setDecision(Boolean decision) {
        this.decision = decision;
    }
}