package ch.uzh.ifi.hase.soprafs21.rest.dto;
import ch.uzh.ifi.hase.soprafs21.entity.Tags;

import java.util.List;

public class ItemPutDTO {
        private Long id;
        private Long userId;
        private String description;
        private String title;
        private List<String> tagsItem;

    public List<String> getTagsItem() {
        return tagsItem;
    }

    public void setTagsItem(List<String> tagsItem) {
        this.tagsItem = tagsItem;
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

