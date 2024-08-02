package com.fdmgroup.accountcustomermngtspring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fdmgroup.accountcustomermngtspring.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
	List<Customer> findByAddressCity(String city);
}
