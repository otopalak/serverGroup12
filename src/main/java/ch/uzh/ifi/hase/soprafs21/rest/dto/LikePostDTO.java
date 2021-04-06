package ch.uzh.ifi.hase.soprafs21.rest.dto;

public class LikePostDTO {
    private Long itemIDSwiper;
    private Long itemIDSwiped;
    private Boolean liked;

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
