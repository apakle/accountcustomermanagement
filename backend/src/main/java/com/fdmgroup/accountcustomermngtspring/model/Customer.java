package com.fdmgroup.accountcustomermngtspring.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Column;

import jakarta.persistence.DiscriminatorColumn;

import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "CUSTOMER_TYPE")
public abstract class Customer {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="customer_id_gen")
	@SequenceGenerator(name = "customer_id_gen", sequenceName = "customer_seq", initialValue = 1001, allocationSize = 1)
    private long customerId;
	@Column(nullable = false)
	@NotBlank(message = "Name is mandatory")
    private String name;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_ADDRESS_ID")
    @Valid
    private Address address;
    
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_CUST_ID")
    private List<Account> accounts = new ArrayList<>();
    
    @Column(name = "CUSTOMER_TYPE", insertable = false, updatable = false)
    private String customerType;
    
    Customer(){}

    public Customer(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public List<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}    
	
	public void addAccount(Account account) {
        this.accounts.add(account);
    }

    public void removeAccount(Account account) {
        this.accounts.remove(account);
    }
    
    public String getCustomerType() {
        return customerType;
    }

}
