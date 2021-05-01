package com.lemsviat.lemhomework252.dao;

import com.lemsviat.lemhomework252.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventsRepository extends JpaRepository<Event,Integer>{
}
