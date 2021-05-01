package com.lemsviat.lemhomework252.dao;

import com.lemsviat.lemhomework252.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Integer>{
}
