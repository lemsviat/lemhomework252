package com.lemsviat.lemhomework252.aws_s3;

import java.io.File;

import com.amazonaws.services.s3.model.*;
import com.lemsviat.lemhomework252.service.AWSS3Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.web.bind.annotation.*;


@RestController
public class AWSS3App {

    private static final AWSCredentials credentials;
    private static final  String bucketName="lem911";

    static {
        credentials = new BasicAWSCredentials(
                "AKIAUKR3AQND2VGZ4YVS",
                "O1VBYkIwciJb6DtiEx933knGa2TSeSc5GuMvYfND"
        );
    }

    AmazonS3 s3client = AmazonS3ClientBuilder
            .standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(Regions.EU_CENTRAL_1)
            .build();

    AWSS3Service awsService = new AWSS3Service(s3client);


    public PutObjectResult putFileToS3(String fileInPC, String fileInS3) {
        return awsService.putObject(
                bucketName,
                fileInPC,
                new File("/Users/Admin/Documents/"+fileInS3));
    }


    public void deleteFileInS3(String fileInS3) {
        awsService.deleteObject(bucketName, fileInS3);
    }

    public S3Object getFileInS3(String fileInS3) {
        return  awsService.getObject(bucketName, fileInS3);
    }
}

