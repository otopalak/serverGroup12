package ch.uzh.ifi.hase.soprafs21.rest.Response;

public class ResponseFile {
    private String name;
    private Long id;
    private String url;
    private String type;
    private long size;


    public ResponseFile(String name, String url, String type, long size,Long id) {
        this.name = name;
        this.url = url;
        this.type = type;
        this.size = size;
        this.id = id;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}