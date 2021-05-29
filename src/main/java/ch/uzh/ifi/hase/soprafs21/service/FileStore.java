package ch.uzh.ifi.hase.soprafs21.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@Service
public class FileStore {

    private final AmazonS3 s3;

    @Autowired
    public FileStore(AmazonS3 s3){
        this.s3 = s3;
    }

    // Path = Bucketname & any subfolders + Any metadata we want to save in AWS Bucket
    public void save(String path, String fileName, Optional<Map<String, String>> optionalMetadata, InputStream inputStream){

        ObjectMetadata metadata = new ObjectMetadata();
        optionalMetadata.ifPresent(map -> {
            if(!map.isEmpty()){
                map.forEach((key,value) -> metadata.addUserMetadata(key,value));
            }
        });

        try {
            s3.putObject(path, fileName, inputStream, metadata);
        }catch (AmazonServiceException e){
            throw new IllegalStateException("Failed to store file to S3",e);

        }

    }
    // Downloads the file M3
    public byte[] download(String path,String key) {
        try {
            S3Object s3Object = s3.getObject(path, key);
            S3ObjectInputStream inputStream = s3Object.getObjectContent();
            return IOUtils.toByteArray(inputStream);

        }
        catch (AmazonServiceException | IOException e) {
            throw new IllegalStateException("Failed to download");
        }
    }
    // Deletes the file from Amazon S3
    public String delete(String path,String key){
        try{
            s3.deleteObject(path,key);
        }catch (Exception e){
            return "Cannot Delete File";
        }
        return "Delete Successfully";
    }



}