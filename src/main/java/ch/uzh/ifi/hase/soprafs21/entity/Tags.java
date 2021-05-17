package ch.uzh.ifi.hase.soprafs21.entity;


import org.mapstruct.Mapping;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
 *  This is a picture Entity to save pictures in the Database
 */

@Entity
@Table(name="tags")
public class Tags implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String description;

    @ManyToMany(mappedBy = "itemtags")
    private List<Item> items = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
