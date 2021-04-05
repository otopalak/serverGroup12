package ch.uzh.ifi.hase.soprafs21.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "MATCH")
public class Match implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long matchID;

    @Column(nullable = false)
    private String itemIDWhoLikes;

    @Column(nullable = false)
    private String itemIDWhoisLiked;

    public Long getMatchID() {
        return matchID;
    }

    public void setMatchID(Long matchID) {
        this.matchID = matchID;
    }

    public String getItemIDWhoLikes() {
        return itemIDWhoLikes;
    }

    public void setItemIDWhoLikes(String itemIDWhoLikes) {
        this.itemIDWhoLikes = itemIDWhoLikes;
    }

    public String getItemIDWhoisLiked() {
        return itemIDWhoisLiked;
    }

    public void setItemIDWhoisLiked(String itemIDWhoisLiked) {
        this.itemIDWhoisLiked = itemIDWhoisLiked;
    }
}


