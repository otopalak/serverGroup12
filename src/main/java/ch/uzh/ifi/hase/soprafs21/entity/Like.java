package ch.uzh.ifi.hase.soprafs21.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "LIKE")
public class Like implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long likeID;

    @Column(nullable = false)
    private String itemIdSwiper;

    @Column(nullable = false)
    private String itemIDSwiped;

    @Column(nullable = false)
    private Boolean liked;


    public Long getLikeID() {
        return likeID;
    }

    public void setLikeID(Long likeID) {
        this.likeID = likeID;
    }

    public String getItemIdSwiper() {
        return itemIdSwiper;
    }

    public void setItemIdSwiper(String itemIDSwiper) {
        this.itemIdSwiper = itemIDSwiper;
    }

    public String getItemIDSwiped() {
        return itemIDSwiped;
    }

    public void setItemIDSwiped(String itemIDSwiped) {
        this.itemIDSwiped = itemIDSwiped;
    }
}
