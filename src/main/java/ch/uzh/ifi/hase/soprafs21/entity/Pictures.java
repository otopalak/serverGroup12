package ch.uzh.ifi.hase.soprafs21.entity;

import javax.persistence.*;
import java.util.List;

/*
 *  This is a picture Entity to save pictures in the Database
 */

@Entity
@Table(name="pictures")
public class Pictures {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private Long itemId;
    @Column
    private String name;
    @Column
    private String type;
    @Column
    private String url;

    public Pictures() {
    super();
    }

    // Constuctor to generate the picture
    public Pictures(Long itemId, String name, String type, String url) {
        this.itemId = itemId;
        this.name = name;
        this.type = type;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
