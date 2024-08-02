package com.fdmgroup.accountcustomermngtspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fdmgroup.accountcustomermngtspring.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
