package ch.uzh.ifi.hase.soprafs21.rest.dto;

import java.util.List;

public class PictureGetDTO {
    private List<String> names;
    private List<String> urls;

    public PictureGetDTO(List<String> names, List<String> urls) {
        this.names = names;
        this.urls = urls;
    }
}
