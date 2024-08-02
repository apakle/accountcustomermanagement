package com.fdmgroup.accountcustomermngtspring;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fdmgroup.accountcustomermngtspring.model.*;
import com.fdmgroup.accountcustomermngtspring.repository.*;

import java.util.Arrays;

@Configuration
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(CustomerRepository customerRepo,
                                   AddressRepository addressRepo,
                                   AccountRepository accountRepo) {
        return args -> {
            // Creating Addresses
            Address address1 = new Address("13 KENWORTH STREET", "T3R 3E3", "TORONTO", "ONTARIO");
            Address address2 = new Address("2 QUEEN STREET", "R43 6Y6", "CALEDON", "ONTARIO");

            // Creating Customers with respective addresses
            Company company = new Company("FDM", address1);
            Person person = new Person("JOHN", address2);

            // Creating Accounts
            SavingsAccount savingsAccount1 = new SavingsAccount(2300.0, 4.5);
            SavingsAccount savingsAccount2 = new SavingsAccount(45000.0, 5.0);
            CheckingAccount checkingAccount = new CheckingAccount(120.0, 1);

            // Adding savingsAccount1 to company and persisting it to the db
            company.addAccount(savingsAccount1);
            customerRepo.save(company);
            
            // Adding savingsAccount2 to person and adding checkingAccount to company
            // and persisting both to the db
            person.addAccount(savingsAccount2);            
            company.addAccount(checkingAccount);   
            customerRepo.save(person);
            customerRepo.save(company);
                     
        };
    }
}
