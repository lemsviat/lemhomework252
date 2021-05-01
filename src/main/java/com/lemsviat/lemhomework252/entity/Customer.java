package com.lemsviat.lemhomework252.entity;

import lombok.Data;
import lombok.extern.java.Log;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "customers")
@Data
@Log
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "acc_id")
    private Account account;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "customer_id")
    private List<File> fileList;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //@Fetch(FetchMode.JOIN)
    @JoinColumn(name = "customer_id")
    private List<Event> eventList;

}
