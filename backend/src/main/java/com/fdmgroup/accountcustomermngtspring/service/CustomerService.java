package com.fdmgroup.accountcustomermngtspring.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.accountcustomermngtspring.model.Account;
import com.fdmgroup.accountcustomermngtspring.model.Customer;
import com.fdmgroup.accountcustomermngtspring.repository.CustomerRepository;

@Service
public class CustomerService {    
    private CustomerRepository customerRepo;
    
    @Autowired
    public CustomerService(CustomerRepository customerRepo) {
		this.customerRepo = customerRepo;
	}

	public Customer createCustomer(Customer customer) {
        return customerRepo.save(customer);
    }

    public Customer getCustomerById(Long id) {
    	Optional<Customer> optCustomer = customerRepo.findById(id);
    	
    	if(optCustomer.isPresent()) {
    		return optCustomer.get();
    	}
    	return null;
    }

    public List<Customer> getAllCustomers() {
        return customerRepo.findAll();
    }

    public Customer updateCustomer(Customer customer) {
    	if(customerRepo.existsById(customer.getCustomerId())) {
    		customerRepo.save(customer);
    		return customer;
    	}
        return null;
    }

    public boolean deleteCustomer(Long id) {
    	if(customerRepo.existsById(id)) {
    		customerRepo.deleteById(id);
    		return true;
    	}
    	return false;
    }
    
    public Customer addAccountToCustomer(Long customerId, Account account) {
        Customer customer = getCustomerById(customerId);
        if (customer != null) {
            customer.addAccount(account);
            return customerRepo.save(customer);
        }
        return null;
    }
}
