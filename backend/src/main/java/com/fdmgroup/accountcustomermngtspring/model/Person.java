package com.fdmgroup.accountcustomermngtspring.model;

import jakarta.persistence.DiscriminatorValue;

import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PERSON")
public class Person extends Customer {

    Person() {}

    public Person(String name, Address address) {
        super(name, address);
    }

}
