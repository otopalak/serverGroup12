package ch.uzh.ifi.hase.soprafs21.entity;


import javax.persistence.*;
import java.io.Serializable;


/*
 *  This is a picture Entity to save pictures in the Database
 */

@Entity
@Table(name="matches")
public class Matches implements Serializable{
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Long itemIdOne;

    @Column(nullable = false)
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