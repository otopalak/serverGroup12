package ch.uzh.ifi.hase.soprafs21.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/*
 *  This is a picture Entity to save pictures in the Database
 */

@Entity
@Table(name="matches")
public class Matches {
    @Id
    @GeneratedValue
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