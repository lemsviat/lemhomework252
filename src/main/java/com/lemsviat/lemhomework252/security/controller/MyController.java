package com.lemsviat.lemhomework252.security.controller;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.lemsviat.lemhomework252.aws_s3.AWSS3App;
import com.lemsviat.lemhomework252.entity.Event;
import com.lemsviat.lemhomework252.entity.File;
import com.lemsviat.lemhomework252.entity.FileStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


@Controller
@Transactional
public class MyController {
    @PersistenceContext
    EntityManager entityManager;

    AWSS3App s3Application = new AWSS3App();
    
    @GetMapping("/")
    public String getInfoForUsers() {
        return "view_for_all_users";
    }

    @GetMapping(value = "/uploadingS3_info")
    public @ResponseBody
    String getUploadedFileInfo(@RequestParam(value = "fileInPC") String fileInPC, @RequestParam(value = "fileInS3") String fileInS3) {
        File file = new File();
        file.setFileName(fileInS3);
        file.setFileStatus(FileStatus.ACTIVE);
        entityManager.merge(file);
        Event event = new Event();
        event.setEventName(fileInS3 + " was uploaded to S3");
        entityManager.merge(event);
        return "File with name " + fileInS3 + " was uploaded to S3 and received ETag "
                + s3Application.putFileToS3(fileInPC, fileInS3).getETag();
    }

    @GetMapping("/deletingS3_info")
    public @ResponseBody
    String getDeletedFileInfo(@RequestParam(value = "fileInS3") String fileInS3) {
        String response;
        try {
            s3Application.getFileInS3(fileInS3);
            s3Application.deleteFileInS3(fileInS3);

            Query query = entityManager.createQuery("update File set fileStatus=:p1 where fileName=:p2");
            query.setParameter("p1", FileStatus.DELETED);
            query.setParameter("p2", fileInS3);
            query.executeUpdate();

            Event event = new Event();
            event.setEventName(fileInS3 + " was deleted from S3");
            entityManager.merge(event);
            response = "File with name " + fileInS3 + " was deleted from S3";
        } catch (AmazonS3Exception e) {
            response = "File with the name in AWS S3 not found!";
        }
        return response;
    }
}
