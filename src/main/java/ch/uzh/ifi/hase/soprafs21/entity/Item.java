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

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String title;

    @Column
    private int reportcount = 0;

    @Column
    private int picturecount = 0;

    @ManyToMany
//    @JoinTable(
//            name = "items_tags",
//            joinColumns = @JoinColumn(name = "item_id"),
//            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tags> itemtags;

    @OneToMany(orphanRemoval = true, mappedBy = "itemIDSwiper")
    private List<Like> likes_Swiper;

    @OneToMany(orphanRemoval = true, mappedBy = "itemIDSwiped")
    private List<Like> likes_Swiped;

    @OneToMany(orphanRemoval = true, mappedBy = "itemIdOne")
    private List<Matches> matches_itemOne;

    @OneToMany(orphanRemoval = true, mappedBy = "itemIdTwo")
    private List<Matches> matches_itemTwo;

//    @OneToMany(orphanRemoval = true, mappedBy = "itemID1")
//    private List<SwapConfirmation> swaps_itemOne;
//
//    @OneToMany(orphanRemoval = true, mappedBy = "itemID2")
//    private List<SwapConfirmation> swaps_itemTwo;

    @OneToMany(orphanRemoval = true, mappedBy = "itemID1")
    private List<SwapConfirmation> swapConfirmations_itemOne;

    @OneToMany(orphanRemoval = true, mappedBy = "itemID2")
    private List<SwapConfirmation> swapConfirmations_itemTwo;

    @ElementCollection
    private List<Long> swapHistory;

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

    public int getReportcount() {
        return reportcount;
    }

    public void setReportcount(int reportcount) {
        this.reportcount = reportcount;
    }

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

    public List<Like> getLikes_Swiper() {
        return likes_Swiper;
    }

    public void setLikes_Swiper(List<Like> likes_Swiper) {
        this.likes_Swiper = likes_Swiper;
    }

    public List<Like> getLikes_Swiped() {
        return likes_Swiped;
    }

    public void setLikes_Swiped(List<Like> likes_Swiped) {
        this.likes_Swiped = likes_Swiped;
    }

    public List<Matches> getMatches_itemOne() {
        return matches_itemOne;
    }

    public void setMatches_itemOne(List<Matches> matches_itemOne) {
        this.matches_itemOne = matches_itemOne;
    }

    public List<Matches> getMatches_itemTwo() {
        return matches_itemTwo;
    }

    public void setMatches_itemTwo(List<Matches> matches_itemTwo) {
        this.matches_itemTwo = matches_itemTwo;
    }

//    public List<SwapConfirmation> getSwaps_itemOne() {
//        return swaps_itemOne;
//    }
//
//    public void setSwaps_itemOne(List<SwapConfirmation> swaps_itemOne) {
//        this.swaps_itemOne = swaps_itemOne;
//    }
//
//    public List<SwapConfirmation> getSwaps_itemTwo() {
//        return swaps_itemTwo;
//    }
//
//    public void setSwaps_itemTwo(List<SwapConfirmation> swaps_itemTwo) {
//        this.swaps_itemTwo = swaps_itemTwo;
//    }

    public List<SwapConfirmation> getSwapConfirmations_itemOne() {
        return swapConfirmations_itemOne;
    }

    public void setSwapConfirmations_itemOne(List<SwapConfirmation> swapConfirmations_itemOne) {
        this.swapConfirmations_itemOne = swapConfirmations_itemOne;
    }

    public List<SwapConfirmation> getSwapConfirmations_itemTwo() {
        return swapConfirmations_itemTwo;
    }

    public void setSwapConfirmations_itemTwo(List<SwapConfirmation> swapConfirmations_itemTwo) {
        this.swapConfirmations_itemTwo = swapConfirmations_itemTwo;
    }

    public List<Long> getSwapHistory() {
        return swapHistory;
    }

    public void setSwapHistory(List<Long> swapHistory) {
        this.swapHistory = swapHistory;
    }
}

