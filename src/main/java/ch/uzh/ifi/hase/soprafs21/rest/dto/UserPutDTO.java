package ch.uzh.ifi.hase.soprafs21.rest.dto;

// This class is for updating the user
public class UserPutDTO {
    // The id is used to identify the user
    private Long id;
    private String username;
    private String password;
    private Double longitude;
    private Double latitude;

    public Double getLongitude() { return longitude; }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
