package com.fdmgroup.accountcustomermngtspring.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("COMPANY")
public class Company extends Customer {

    Company() {}

    public Company(String name, Address address) {
        super(name, address);
    }
}
