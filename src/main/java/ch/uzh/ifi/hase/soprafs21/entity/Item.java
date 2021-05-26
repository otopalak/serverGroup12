package ch.uzh.ifi.hase.soprafs21.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/*
 *  This is a picture Entity to save pictures in the Database
 */

@Entity
@Table(name="appItems")
public class Item implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(columnDefinition="TEXT")
    private String description;

    @Column(nullable = false)
    private String title;

    @Column
    private int reportcount = 0;

    @Column
    private int picturecount = 0;

    @ManyToMany
    private List<Tags> itemtags = new ArrayList<>();

    @OneToMany(orphanRemoval = true, mappedBy = "itemIDSwiper")
    private List<Like> likes_Swiper;

    @OneToMany(orphanRemoval = true, mappedBy = "itemIDSwiped")
    private List<Like> likes_Swiped;

    @OneToMany(orphanRemoval = true, mappedBy = "itemIdOne")
    private List<Matches> matches_itemOne;

    @OneToMany(orphanRemoval = true, mappedBy = "itemIdTwo")
    private List<Matches> matches_itemTwo;

    @OneToMany(orphanRemoval = true, mappedBy = "itemID1")
    private List<SwapConfirmation> swaps_itemOne;

    @OneToMany(orphanRemoval = true, mappedBy = "itemID2")
    private List<SwapConfirmation> swaps_itemTwo;

    @OneToMany(orphanRemoval = true, mappedBy = "itemID1")
    private List<SwapConfirmation> swapConfirmations_itemOne;

    @OneToMany(orphanRemoval = true, mappedBy = "itemID2")
    private List<SwapConfirmation> swapConfirmations_itemTwo;

    @ElementCollection
    private List<Long> swapHistory;

    public int getPicturecount() {
        return picturecount;
    }

    public void setPicturecount(int picturecount) {
        this.picturecount = picturecount;
    }

    public List<Tags> getItemtags() {
        return itemtags;
    }

    public void setItemtags(List<Tags> itemtags) {
        this.itemtags = itemtags;
    }

    public int getReportcount() {
        return reportcount;
    }

    public void setReportcount(int reportcount) {
        this.reportcount = reportcount;
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

    public List<Long> getSwapHistory() {
        return swapHistory;
    }

    public void setSwapHistory(List<Long> swapHistory) {
        this.swapHistory = swapHistory;
    }
}

