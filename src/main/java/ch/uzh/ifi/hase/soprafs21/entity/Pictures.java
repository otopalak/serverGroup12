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
    private Long itemId;
    private String name;
    private String type;


    // Needed to save Blob's => Storing binary data
    @Lob
    private byte[] data;

    // Empty Constructor
    public Pictures(){
    }

    // Non empty Constructor
    public Pictures(String name, String type, byte[] data,Long itemId) {
        this.name = name;
        this.type = type;
        this.data = data;
        this.itemId = itemId;
    }
    // Here we have all Getters and Setters:


    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
