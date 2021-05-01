package com.lemsviat.lemhomework252.entity;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "events")
@Data
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Integer eventId;

    @Column(name = "event_name")
    private String eventName;

    public Event() {
    }
}
