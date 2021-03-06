package com.lemsviat.lemhomework252.security.controller;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.lemsviat.lemhomework252.aws_s3.AWSS3App;
import com.lemsviat.lemhomework252.entity.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Controller
@Transactional
public class MyController {
    @PersistenceContext
    EntityManager entityManager;


    Set<File> fileSet = new HashSet<>();
    List<Event> eventList = new ArrayList<>();

    AWSS3App s3Application = new AWSS3App();

    @GetMapping("/")
    public String getInfoForUsers() {
        return "view_for_all_users";
    }

    @GetMapping(value = "/uploadingS3_info")
    public @ResponseBody
    String getUploadedFileInfo(@RequestParam(value = "fileInPC") String fileInPC, @RequestParam(value = "fileInS3") String fileInS3) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName=authentication.getName();

        Query q = entityManager.createQuery("select c from Customer c where c.name=:n1");
        q.setParameter("n1", currentPrincipalName);
        Customer customer;
        customer = (Customer) q.getSingleResult();

        File file = new File();
        file.setFileName(fileInS3);
        file.setFileStatus(FileStatus.ACTIVE);
        fileSet =customer.getFileSet();
        fileSet.add(file);
        customer.setFileSet(fileSet);

        Event event = new Event();
        event.setEventName(fileInS3 + " was uploaded to S3");
        eventList=customer.getEventList();
        eventList.add(event);
        customer.setEventList(eventList);

        entityManager.merge(customer);

        System.out.println(customer);
        System.out.println(currentPrincipalName);

        return "File with name " + fileInS3 + " was uploaded to S3 and received ETag "
                + s3Application.putFileToS3(fileInPC, fileInS3).getETag();
    }

    @GetMapping("/deletingS3_info")
    public @ResponseBody
    String getDeletedFileInfo(@RequestParam(value = "fileInS3") String fileInS3) {
        String response;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName=authentication.getName();

        try {
            s3Application.getFileInS3(fileInS3);
            s3Application.deleteFileInS3(fileInS3);

            Query query = entityManager.createQuery("update File set fileStatus=:p1 where fileName=:p2");
            query.setParameter("p1", FileStatus.DELETED);
            query.setParameter("p2", fileInS3);
            query.executeUpdate();

            Query q = entityManager.createQuery("select c from Customer c where c.name=:n1");
            q.setParameter("n1", currentPrincipalName);
            Customer customer;
            customer = (Customer) q.getSingleResult();

            Event event = new Event();
            event.setEventName(fileInS3 + " was deleted from S3");
            eventList=customer.getEventList();
            eventList.add(event);
            customer.setEventList(eventList);

            entityManager.merge(customer);
            response = "File with name " + fileInS3 + " was deleted from S3";
        } catch (AmazonS3Exception e) {
            response = "File with the name in AWS S3 not found!";
        }
        return response;
    }

    @GetMapping("/customers")
    public @ResponseBody
    List<Customer> findAll() {
        Query q = entityManager.createQuery("select c from Customer c");
        List<Customer> customersList;
        customersList = q.getResultList();
        return customersList;
    }


    @PostMapping(value = "/customers")
    public @ResponseBody
    String saveNewCustomer(@RequestParam(value = "username") String username,
                           @RequestParam(value = "account") String account,
                           @RequestParam(value = "accountStatus") String accountStatus) {
        Account newAccount = new Account();
        newAccount.setAccountValue(Long.parseLong(account));
        newAccount.setAccountStatus(AccountStatus.valueOf(accountStatus));
        Customer customer = new Customer();
        customer.setName(username);
        customer.setAccount(newAccount);
        entityManager.merge(customer);

        return "Customer with name " + username + " was saved to DB";
    }
}
