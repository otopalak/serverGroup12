package ch.uzh.ifi.hase.soprafs21.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


/*
 *  This is a picture Entity to save pictures in the Database
 */

@Entity
@Table(name="item")
public class Item {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String title;

    // Tags for the item
    @ManyToMany
    private List<Tags> itemtags = new ArrayList<>();


    public List<Tags> getItemtags() {
        return itemtags;
    }


    public void setItemtags(List<Tags> itemtags) {
        this.itemtags = itemtags;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

