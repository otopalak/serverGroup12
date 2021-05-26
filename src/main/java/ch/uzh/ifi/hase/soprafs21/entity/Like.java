package ch.uzh.ifi.hase.soprafs21.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "LIKES") // "LIKE" funktioniert nicht aus irgendeinem Grund...
public class Like implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long likeID;

    @Column(nullable = false)
    private Long itemIDSwiper;

    @Column(nullable = false)
    private Long itemIDSwiped;

    @Column(nullable = false)
    private Boolean liked;

//    @OneToMany(orphanRemoval = true, mappedBy = "itemIdOne")
//    private List<Matches> matches_itemOne;
//
//    @OneToMany(orphanRemoval = true, mappedBy = "itemIdTwo")
//    private List<SwapConfirmation> matches_itemTwo;


    public Long getLikeID() {
        return likeID;
    }

    public void setLikeID(Long likeID) {
        this.likeID = likeID;
    }

    public Long getItemIDSwiper() {
        return itemIDSwiper;
    }

    public void setItemIDSwiper(Long itemIDSwiper) {
        this.itemIDSwiper = itemIDSwiper;
    }

    public Long getItemIDSwiped() {
        return itemIDSwiped;
    }

    public void setItemIDSwiped(Long itemIDSwiped) {
        this.itemIDSwiped = itemIDSwiped;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }
}
