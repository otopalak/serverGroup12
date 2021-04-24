package ch.uzh.ifi.hase.soprafs21.bucket;

public enum BucketName {

    Item_Image("mypicturegallerydshush");
    private final String bucketName;

    BucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}
