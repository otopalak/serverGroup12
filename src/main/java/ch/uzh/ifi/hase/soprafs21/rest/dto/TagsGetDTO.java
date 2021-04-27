package ch.uzh.ifi.hase.soprafs21.rest.dto;

import java.util.List;
// Get DTO to receive all Items by the different Tags
public class TagsGetDTO {
    private List<String> tags;

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
