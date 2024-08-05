package com.fdmgroup.accountcustomermngtspring.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fdmgroup.accountcustomermngtspring.model.Account;
import com.fdmgroup.accountcustomermngtspring.model.Customer;
import com.fdmgroup.accountcustomermngtspring.repository.AccountRepository;
import com.fdmgroup.accountcustomermngtspring.repository.CustomerRepository;

@Service
public class AccountService {
    private AccountRepository accountRepo;
    private CustomerRepository customerRepo;

    @Autowired
    public AccountService(AccountRepository accountRepo, CustomerRepository customerRepo) {
        this.accountRepo = accountRepo;
        this.customerRepo = customerRepo;
    }

    public Account createAccount(Account account, Long customerId) {
        Customer customer = customerRepo.findById(customerId).orElse(null);
        if (customer != null) {
        	accountRepo.save(account);
            customer.addAccount(account);
            customerRepo.save(customer); 
            return account; 
        }
        return null;
    }

    public Account getAccountById(Long id) {
        return accountRepo.findById(id).orElse(null);
    }

    public List<Account> getAllAccounts() {
        return accountRepo.findAll();
    }

    public Account updateAccount(Account account) {
        if (accountRepo.existsById(account.getAccountId())) {
            return accountRepo.save(account);
        }
        return null;
    }

    public boolean deleteAccount(Long id) {
        if (accountRepo.existsById(id)) {
            accountRepo.deleteById(id);
            return true;
        }
        return false;
    }
    
    public List<Account> getAccountsByCustomerCity(String city) {
        List<Customer> customers = customerRepo.findByAddressCity(city);
        List<Account> accounts = new ArrayList<>();
        for (Customer customer : customers) {
            accounts.addAll(customer.getAccounts());
        }
        return accounts;
    }
    
    public Account deposit(Long accountId, double amount) {
        Account account = accountRepo.findById(accountId).orElse(null);
        if (account != null) {
        	account.deposit(amount);
        	return accountRepo.save(account);
        }
        return null; 
    }
}
